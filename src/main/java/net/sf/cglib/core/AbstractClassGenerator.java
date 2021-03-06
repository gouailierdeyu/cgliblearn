/*
 * Copyright 2003,2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.cglib.core;

import net.sf.cglib.core.internal.Function;
import net.sf.cglib.core.internal.LoadingCache;
import org.objectweb.asm.ClassReader;

import java.lang.ref.WeakReference;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.WeakHashMap;

/**
 * Enhancer增强类的父类，用来动态生成T类型的子类，可以缓存生成的类
 * Abstract class for all code-generating CGLIB utilities.
 * In addition to caching generated classes for performance, it provides hooks for
 * customizing the <code>ClassLoader</code>, name of the generated class, and transformations
 * applied before generation.
 *
 */
abstract public class AbstractClassGenerator<T>
implements ClassGenerator
{
    private static final ThreadLocal CURRENT = new ThreadLocal();

    /**
     * WeakHashMap 用来缓存类加载器，类加载器数据
     */
    private static volatile Map<ClassLoader, ClassLoaderData> CACHE = new WeakHashMap<ClassLoader, ClassLoaderData>();

    private static final boolean DEFAULT_USE_CACHE =
        Boolean.parseBoolean(System.getProperty("cglib.useCache", "true"));

    /**
     * 生成策略，命名生成类的策略
     */
    private GeneratorStrategy strategy = DefaultGeneratorStrategy.INSTANCE;
    private NamingPolicy namingPolicy = DefaultNamingPolicy.INSTANCE;
    /**
     * 包含本类的类名
     */
    private Source source;
    private ClassLoader classLoader;

    /**
     * 生成类的前缀，通常是设置的父类名（被代理的父类名）
     */
    private String namePrefix;

    /**
     * key 是key接口（keyinterface）的全限定类名
     *
     */
    private Object key;
    private boolean useCache = DEFAULT_USE_CACHE;
    private String className;
    private boolean attemptLoad;

    /**
     * 主要包含类加载器加载的类名的集合，
     * 缓存生成类。只要生成类的类加载器可访问，生成类就一直缓存着，一直可用
     * 缓存类加载器WeakReference
     */
    protected static class ClassLoaderData {
        private final Set<String> reservedClassNames = new HashSet<String>();

        /**
         * {@link AbstractClassGenerator} here holds "cache key" (e.g. {@link net.sf.cglib.proxy.Enhancer}
         * configuration), and the value is the generated class plus some additional values
         * (see {@link #unwrapCachedValue(Object)}.
         * <p>The generated classes can be reused as long as their classloader is reachable.</p>
         * <p>Note: the only way to access a class is to find it through generatedClasses cache, thus
         * the key should not expire as long as the class itself is alive (its classloader is alive).</p>
         */
        private final LoadingCache<AbstractClassGenerator, Object, Object> generatedClasses;

        /**
         * Note: ClassLoaderData object is stored as a value of {@code WeakHashMap<ClassLoader, ...>} thus
         * this classLoader reference should be weak otherwise it would make classLoader strongly reachable
         * and alive forever.
         * Reference queue is not required since the cleanup is handled by {@link WeakHashMap}.
         */
        private final WeakReference<ClassLoader> classLoader;

        /**
         * 断言
         */
        private final Predicate uniqueNamePredicate = new Predicate() {
            @Override
            public boolean evaluate(Object name) {
                return reservedClassNames.contains(name);
            }
        };

        /**
         * 函数   参数是AbstractClassGenerator  返回值是Object
         */
        private static final Function<AbstractClassGenerator, Object> GET_KEY = new Function<AbstractClassGenerator, Object>() {
            @Override
            public Object apply(AbstractClassGenerator gen) {
                return gen.key;
            }
        };

        public ClassLoaderData(ClassLoader classLoader) {
            if (classLoader == null) {
                throw new IllegalArgumentException("classLoader == null is not yet supported");
            }
            this.classLoader = new WeakReference<ClassLoader>(classLoader);
            Function<AbstractClassGenerator, Object> load =
                    new Function<AbstractClassGenerator, Object>() {
                        @Override
                        public Object apply(AbstractClassGenerator gen) {
                            Class klass = gen.generate(ClassLoaderData.this);
                            return gen.wrapCachedClass(klass);
                        }
                    };
            generatedClasses = new LoadingCache<AbstractClassGenerator, Object, Object>(GET_KEY, load);
        }

        public ClassLoader getClassLoader() {
            return classLoader.get();
        }

        public void reserveName(String name) {
            reservedClassNames.add(name);
        }

        public Predicate getUniqueNamePredicate() {
            return uniqueNamePredicate;
        }

        /**
         * 重新生成一个类的类型（class）或者从缓存中取出类的实例
         * @param gen 生成器实例
         * @param useCache 是否使用缓存
         * @return 子类类的类型或者实例
         */
        public Object get(AbstractClassGenerator gen, boolean useCache) {
            if (!useCache) {
              return gen.generate(ClassLoaderData.this);
            } else {
              Object cachedValue = generatedClasses.get(gen);
              return gen.unwrapCachedValue(cachedValue);
            }
        }
    }

    protected T wrapCachedClass(Class klass) {
        return (T) new WeakReference(klass);
    }

    protected Object unwrapCachedValue(T cached) {
        return ((WeakReference) cached).get();
    }

    protected static class Source {
        String name;
        public Source(String name) {
            this.name = name;
        }
    }

    protected AbstractClassGenerator(Source source) {
        this.source = source;
    }

    protected void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    final protected String getClassName() {
        return className;
    }

    private void setClassName(String className) {
        this.className = className;
    }

    /**
     * 使用命名策略生成子类的全类名
     * @param nameTestPredicate 类名字重名判断
     * @return 生成的类名
     */
    private String generateClassName(Predicate nameTestPredicate) {
        return namingPolicy.getClassName(namePrefix, source.name, key, nameTestPredicate);
    }

    /**
     * Set the <code>ClassLoader</code> in which the class will be generated.
     * Concrete subclasses of <code>AbstractClassGenerator</code> (such as <code>Enhancer</code>)
     * will try to choose an appropriate default if this is unset.
     * <p>
     * Classes are cached per-<code>ClassLoader</code> using a <code>WeakHashMap</code>, to allow
     * the generated classes to be removed when the associated loader is garbage collected.
     * @param classLoader the loader to generate the new class with, or null to use the default
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Override the default naming policy.
     * @see DefaultNamingPolicy
     * @param namingPolicy the custom policy, or null to use the default
     */
    public void setNamingPolicy(NamingPolicy namingPolicy) {
        if (namingPolicy == null)
            namingPolicy = DefaultNamingPolicy.INSTANCE;
        this.namingPolicy = namingPolicy;
    }

    /**
     * @see #setNamingPolicy
     */
    public NamingPolicy getNamingPolicy() {
        return namingPolicy;
    }

    /**
     * Whether use and update the static cache of generated classes
     * for a class with the same properties. Default is <code>true</code>.
     */
    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    /**
     * @see #setUseCache
     */
    public boolean getUseCache() {
        return useCache;
    }

    /**
     * If set, CGLIB will attempt to load classes from the specified
     * <code>ClassLoader</code> before generating them. Because generated
     * class names are not guaranteed to be unique, the default is <code>false</code>.
     */
    public void setAttemptLoad(boolean attemptLoad) {
        this.attemptLoad = attemptLoad;
    }

    public boolean getAttemptLoad() {
        return attemptLoad;
    }

    /**
     * Set the strategy to use to create the bytecode from this generator.
     * By default an instance of {@see DefaultGeneratorStrategy} is used.
     */
    public void setStrategy(GeneratorStrategy strategy) {
        if (strategy == null)
            strategy = DefaultGeneratorStrategy.INSTANCE;
        this.strategy = strategy;
    }

    /**
     * @see #setStrategy
     */
    public GeneratorStrategy getStrategy() {
        return strategy;
    }

    /**
     * Used internally by CGLIB. Returns the <code>AbstractClassGenerator</code>
     * that is being used to generate a class in the current thread.
     */
    public static AbstractClassGenerator getCurrent() {
        return (AbstractClassGenerator)CURRENT.get();
    }

    public ClassLoader getClassLoader() {
        ClassLoader t = classLoader;
        if (t == null) {
            t = getDefaultClassLoader();
        }
        if (t == null) {
            t = getClass().getClassLoader();
        }
        if (t == null) {
            t = Thread.currentThread().getContextClassLoader();
        }
        if (t == null) {
            throw new IllegalStateException("Cannot determine classloader");
        }
        return t;
    }

    abstract protected ClassLoader getDefaultClassLoader();

    /**
     * 保护域（default public private）  默认为null default
     * Returns the protection domain to use when defining the class.
     * <p>
     * Default implementation returns <code>null</code> for using a default protection domain. Sub-classes may
     * override to use a more specific protection domain.
     * </p>
     *
     * @return the protection domain (<code>null</code> for using a default)
     */
    protected ProtectionDomain getProtectionDomain() {
    	return null;
    }

    /**
     * key 是key接口的全限定类名
     * @param key key 是key接口的全限定类名
     * @return 返回实现key接口的子类
     */
    protected Object create(Object key) {
        try {
            ClassLoader loader = getClassLoader();
            Map<ClassLoader, ClassLoaderData> cache = CACHE;
            ClassLoaderData data = cache.get(loader);
            if (data == null) {
                synchronized (AbstractClassGenerator.class) {
                    cache = CACHE;
                    data = cache.get(loader);
                    if (data == null) {
                        Map<ClassLoader, ClassLoaderData> newCache = new WeakHashMap<ClassLoader, ClassLoaderData>(cache);
                        data = new ClassLoaderData(loader);
                        newCache.put(loader, data);
                        CACHE = newCache;
                    }
                }
            }
            this.key = key;
            Object obj = data.get(this, getUseCache());
            // 重新生成的  obj是类的类型（Class）走firstInstance（）
            // 从缓存中取出就是子类的实例，走nextInstance()
            // 但是这两个方法的具体实现，可以自定义
            if (obj instanceof Class) {
                return firstInstance((Class) obj);
            }
            return nextInstance(obj);
        } catch (RuntimeException e) {
            throw e;
        } catch (Error e) {
            throw e;
        } catch (Exception e) {
            throw new CodeGenerationException(e);
        }
    }

    /**
     * 根据类加载器相关数据，生成类字节码，再生成类
     * @param data 类加载器数据
     * @return 生成类的class。
     */
    protected Class generate(ClassLoaderData data) {
        Class gen;
        Object save = CURRENT.get();
        CURRENT.set(this);
        try {
            ClassLoader classLoader = data.getClassLoader();
            if (classLoader == null) {
                throw new IllegalStateException("ClassLoader is null while trying to define class " +
                        getClassName() + ". It seems that the loader has been expired from a weak reference somehow. " +
                        "Please file an issue at cglib's issue tracker.");
            }
            synchronized (classLoader) {
              String name = generateClassName(data.getUniqueNamePredicate());
              data.reserveName(name);
              this.setClassName(name);
            }
            if (attemptLoad) {
                try {
                    gen = classLoader.loadClass(getClassName());
                    return gen;
                } catch (ClassNotFoundException e) {
                    // ignore
                }
            }
            /**
             *  这里逻辑比较重要，是主要的生成类的策略
             *  其中会调用自定义实现的generateClass(cw);函数来实际生成类的字节码。
             */
            byte[] b = strategy.generate(this); //默认策略构建类的字节码
            // new ClassReader(b)这个b就是整个类的字节码，建一个类读取器，保存类的所有信息
            String className = ClassNameReader.getClassName(new ClassReader(b));
            ProtectionDomain protectionDomain = getProtectionDomain();
            synchronized (classLoader) { // just in case
                if (protectionDomain == null) {
                    gen = ReflectUtils.defineClass(className, b, classLoader);
                } else {
                    gen = ReflectUtils.defineClass(className, b, classLoader, protectionDomain);
                }
            }
            return gen;
        } catch (RuntimeException e) {
            throw e;
        } catch (Error e) {
            throw e;
        } catch (Exception e) {
            throw new CodeGenerationException(e);
        } finally {
            CURRENT.set(save);
        }
    }

    abstract protected Object firstInstance(Class type) throws Exception;
    abstract protected Object nextInstance(Object instance) throws Exception;
}

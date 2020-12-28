/*
 * Copyright 2003 The Apache Software Foundation
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
package net.sf.cglib.proxy;

/**
 * All callback interfaces used by {@link Enhancer} extend this interface.
 * @see MethodInterceptor  方法拦截器 可以拦截方法做一些增强，代理类实例执行方法返回结果必须和原方法的类型兼容，即子类。
 * @see NoOp   什么也不做，调用生成类的父类的原方法
 * @see LazyLoader 设置可以调用原始方法的对象，增强实例中的第一个惰性加载方法被调用时立即调用。然后，这个对象将用于将来对代理实例的每个方法调用。
 * @see Dispatcher 设置可以调用原始方法的对象，这个方法每次都会调用，不会惰性加载
 * @see InvocationHandler  和java反射的同一个意思，完全代理了生成类执行的方法
 * @see FixedValue 强制对代理对象的方法的返回值进一步处理，返回一个固定值
 */
public interface Callback //不同类型的回调接口父类
{
}

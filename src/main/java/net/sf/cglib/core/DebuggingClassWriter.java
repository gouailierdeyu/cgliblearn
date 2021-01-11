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

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.*;
import java.lang.reflect.Constructor;

/**
 * ClassVisitor的子类都是一些需要对类进行访问的类。需要构造类中属性，，方法的类
 * ClassReader是对类字节码进行解析，解析出常量，类名，方法名等等
 * 将生成的类的字节码返回，或者设置了打印.class文件的话，将字节码输出到文件中
 */
public class DebuggingClassWriter extends ClassVisitor {

    public static final String DEBUG_LOCATION_PROPERTY = "cglib.debugLocation";

    private static String debugLocation;
    private static Constructor traceCtor;

    private String className;
    private String superName;

    static {
        debugLocation = System.getProperty(DEBUG_LOCATION_PROPERTY);
        if (debugLocation != null) {
            System.err.println("CGLIB debugging enabled, writing to '" + debugLocation + "'");
            try {
              Class clazz = Class.forName("org.objectweb.asm.util.TraceClassVisitor");
              traceCtor = clazz.getConstructor(new Class[]{ClassVisitor.class, PrintWriter.class});
            } catch (Throwable ignore) {
            }
        }
    }

    public DebuggingClassWriter(int flags) {
	super(Constants.ASM_API, new ClassWriter(flags));
    }

    @Override
    public void visit(int version,
                      int access,
                      String name,
                      String signature,
                      String superName,
                      String[] interfaces) {
        className = name.replace('/', '.');
        this.superName = superName.replace('/', '.');
        super.visit(version, access, name, signature, superName, interfaces);
    }

    public String getClassName() {
        return className;
    }

    public String getSuperName() {
        return superName;
    }

    public byte[] toByteArray() {

      return (byte[]) java.security.AccessController.doPrivileged(
        new java.security.PrivilegedAction() {
            @Override
            public Object run() {


                byte[] b = ((ClassWriter) DebuggingClassWriter.super.cv).toByteArray();
                if (debugLocation != null) {
                    String dirs = className.replace('.', File.separatorChar);
                    try {
                        new File(debugLocation + File.separatorChar + dirs).getParentFile().mkdirs();

                        File file = new File(new File(debugLocation), dirs + ".class");
                        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                        try {
                            out.write(b);
                        } finally {
                            out.close();
                        }

                        if (traceCtor != null) {
                            file = new File(new File(debugLocation), dirs + ".asm");
                            out = new BufferedOutputStream(new FileOutputStream(file));
                            try {
                                ClassReader cr = new ClassReader(b);
                                PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
                                ClassVisitor tcv = (ClassVisitor)traceCtor.newInstance(new Object[]{null, pw});
                                cr.accept(tcv, 0);
                                pw.flush();
                            } finally {
                                out.close();
                            }
                        }
                    } catch (Exception e) {
                        throw new CodeGenerationException(e);
                    }
                }
                return b;
             }
            });

        }
    }

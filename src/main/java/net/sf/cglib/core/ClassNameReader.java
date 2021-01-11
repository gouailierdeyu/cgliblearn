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
package net.sf.cglib.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.util.*;

/**
 * 获取类名
 * 作者接下来要做的事，是利用ClassReader缓存要找的那个类的类名，在执行accept方法之前
 * 估计是读取一个类的字节码特别耗时，所以准备缓存一下，同时也提供了早退出accept的方法。
 */
// TODO: optimize (ClassReader buffers entire class before accept)
public class ClassNameReader {
    private ClassNameReader() {
    }

    /**
     * EARLY_EXIT这个异常是为了在accept方法执行中，
     * 执行完ClassVisitor.visit()之后，立马退出方法。
     * 聪明，利用运行时异常提前退出一个方法体。
     */
    private static final EarlyExitException EARLY_EXIT = new EarlyExitException();
    private static class EarlyExitException extends RuntimeException { }

    public static String getClassName(ClassReader r) {

        return getClassInfo(r)[0];

    }

    public static String[] getClassInfo(ClassReader r) {
        final List array = new ArrayList();
        // 获取类名信息 比如public class java.a.b extends c implement intera
        // 得到java.a.b
        try {
            r.accept(new ClassVisitor(Constants.ASM_API, null) {
                public void visit(int version,
                                  int access,
                                  String name,
                                  String signature,
                                  String superName,
                                  String[] interfaces) {
                    array.add( name.replace('/', '.') );
                    if(superName != null){
                      array.add( superName.replace('/', '.') );
                    }
                    for(int i = 0; i < interfaces.length; i++  ){
                       array.add( interfaces[i].replace('/', '.') );
                    }

                    throw EARLY_EXIT;
                }
            }, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        } catch (EarlyExitException e) { }

        return (String[])array.toArray( new String[]{} );
    }
}

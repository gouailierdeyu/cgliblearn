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

import java.lang.reflect.Method;

/**
 * {@link java.lang.reflect.InvocationHandler} replacement (unavailable under JDK 1.2).
 * This callback type is primarily for use by the {@link Proxy} class but
 * may be used with {@link Enhancer} as well.
 * @author Neeme Praks <a href="mailto:neeme@apache.org">neeme@apache.org</a>
 * @version $Id: InvocationHandler.java,v 1.3 2004/06/24 21:15:20 herbyderby Exp $
 * 和java反射的同一个意思，完全代理了生成类执行的函数
 * 通过invoke实现
 */

public interface InvocationHandler
extends Callback
{
    /**
     * @see java.lang.reflect.InvocationHandler#invoke(Object, Method, Object)
     * @param  proxy 代理类（生成类）的实例，可以返回这个实例，实现链式函数proxy.do().do().do()
     * @param method 父类执行的函数
     * @param args 父类执行函数的参数数组
     * @return object 代理执行完函数的返回值
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;

}

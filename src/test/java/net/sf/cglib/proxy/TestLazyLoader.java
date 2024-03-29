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
package net.sf.cglib.proxy;

import net.sf.cglib.CodeGenTestCase;
import java.lang.reflect.*;
import java.util.*;
import junit.framework.*;

public class TestLazyLoader extends CodeGenTestCase  {
    public void testLazyLoader() {
        LazyLoader loader = new LazyLoader() {
                public Object loadObject() {
                    System.out.println("loading object");
                    return "foo";
                }
            };
        Object obj = Enhancer.create(Object.class, loader);
        System.out.println(obj.toString());
        assertTrue("foo".equals(obj.toString()));
    }

    public TestLazyLoader(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TestLazyLoader.class);
    }

    public void perform(ClassLoader loader) throws Throwable {
    }

    public void testFailOnMemoryLeak() throws Throwable {
    }

}

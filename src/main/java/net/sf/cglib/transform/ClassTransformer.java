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
package net.sf.cglib.transform;

import net.sf.cglib.core.Constants;
import org.objectweb.asm.ClassVisitor;

/**
 * 在cglib中对class的访问和asm对class的访问之间的一个恰当转换层
 */
public abstract class ClassTransformer extends ClassVisitor {
    public ClassTransformer() {
	super(Constants.ASM_API);
    }
    public ClassTransformer(int opcode) {
	super(opcode);
    }
   public abstract void setTarget(ClassVisitor target);
}

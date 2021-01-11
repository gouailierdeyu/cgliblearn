package net.sf.cglib.core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * UTF-8
 * Created by czy  Time : 2021/1/11 16:33
 *
 * @version 1.0
 */
public class ClassEmitterTest {
    @Test
    public void testClassEmitter(){
        ClassEmitter classEmitter = new ClassEmitter();
        System.out.println(classEmitter.getClassInfo());
        System.out.println("hello world");
    }

}

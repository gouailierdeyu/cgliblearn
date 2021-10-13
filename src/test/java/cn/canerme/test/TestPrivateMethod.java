package cn.canerme.test;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * UTF-8
 * Created by czy  Time : 2021/3/10 20:29
 *
 * @version 1.0
 */

/**
 * 不能代理private和final的方法
 */
public class TestPrivateMethod {
    @Test
    public void testPrivateMethod(){
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,"E:\\study\\java\\cgliblearn\\src\\main\\java\\enhancer");
        Enhancer enhancer= new Enhancer();
        enhancer.setCallback(new PrintTest());

        //enhancer.setCallbackType(czy.PrintCallBack.class);
        enhancer.setSuperclass(TestPrivateMethod.class);
        enhancer.setUseFactory(false);
        TestPrivateMethod test=(TestPrivateMethod) enhancer.create();
        String hh= test.print("czy.testcglib");
    }

    public  String print(String str){
        System.out.println("原始函数执行："+str);
        privateMethod();
        publicMethod();
        return "456";
    }

    private void privateMethod(){
        System.out.println("原始私有方法");
    }

    public void publicMethod(){
        System.out.println("原始公有方法");
    }
}
class PrintTest implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("===========");
        System.out.println(method.getName());
        System.out.println(proxy.invokeSuper(obj,args));
        System.out.println("============");
        return null;
    }
}

package cn.canerme.test;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.*;

/**
 * UTF-8
 * Created by czy  Time : 2021/2/4 16:50
 *
 * @version 1.0
 */
public class TestClassCGlib {

    @Test
    public void testClassCGlib(){
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,"E:\\study\\java\\cgliblearn\\src\\main\\java\\enhancer");
        Enhancer enhancer= new Enhancer();
        enhancer.setCallback(new PrintInvo());

        //enhancer.setCallbackType(czy.PrintCallBack.class);
        enhancer.setSuperclass(TestClassCGlib.class);
        enhancer.setUseFactory(false);
        TestClassCGlib test=(TestClassCGlib) enhancer.create();
        System.out.println(test.getClass());
        System.out.println("查看生成子类");
        //实际上是访问生成子类的toString方法和其他方法，
        // 生成类被cglib修改了，所以会报错
        // System.out.println( test);
//        System.out.println(test.CGLIB$toString$2());
        System.out.println("结束查看生成子类");
        String hh= test.print("czy.testcglib");
        System.out.println(hh);
    }

    public  String print(String str){
        System.out.println("原始函数执行："+str);
        return "456";
    }
}
class PrintInvo implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //System.out.println(proxy);
//        method.invoke(proxy,args);
        System.out.println(method.getName());
        Arrays.stream(args).forEach((a)-> {
            System.out.println(a.toString());
        });
        System.out.println("Print call back");
        return "ss";
    }
}

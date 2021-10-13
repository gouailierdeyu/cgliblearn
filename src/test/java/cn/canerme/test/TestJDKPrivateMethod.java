package cn.canerme.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * UTF-8
 * Created by czy  Time : 2021/3/10 20:57
 *
 * @version 1.0
 */
public class TestJDKPrivateMethod implements Print{
     public void print(int a, int b) {
        System.out.println("a: "+a+" b:"+b);
    }

    public static void main(String[] args) {
        Print subject=new TestJDKPrivateMethod();
        Print subjectProxy=(Print) Proxy.newProxyInstance(Print.class.getClassLoader(), TestJDKPrivateMethod.class.getInterfaces(), new ProxyInvocationHandler(subject));
        subjectProxy.print(2,5);
    }
}

interface Print {
    void print(int a,int b);
}
class ProxyInvocationHandler implements InvocationHandler {
    private Print target;
    public ProxyInvocationHandler(Print target) {
        this.target=target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.print("say:");
        return method.invoke(target, args);
    }

}

//class ProxyInvocationHandler implements java.lang.reflect.InvocationHandler {
//@Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println(method);
//        if (method.getName().equals("morning")) {
//        System.out.println("Good morning, " + args[0]);
//        }
//        return null;
//    }
//}
//class HelloWorld  implements Hello {
//    public void morning(String name) {
//        System.out.println("Good morning, " + name);
//    }
//    public void ss(){
//        System.out.println("gsuydguysagbduyg");
//    }
//}
//interface Hello {
//    void morning(String name);
//}
//
//public class TestJDKPrivateMethod {
//    public static void main(String[] args) {
//        Hello hello = (Hello) Proxy.newProxyInstance(
//                Hello.class.getClassLoader(), // 传入ClassLoader
//                HelloWorld.class.getInterfaces(), // 传入要实现的接口
//                new ProxyInvocationHandler()); // 传入处理调用方法的InvocationHandler
//        hello.morning("Bob");
//
//    }
//}

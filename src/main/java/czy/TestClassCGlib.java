package czy;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.*;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/17 10:56
 *
 * @version 1.0
 * cglib实现的代理，
 * 实质是生成一个设置在Enhancer中的已知类（通过setSuperclass()方法）的子类
 * 子类代理了父类的操作
 */
public class TestClassCGlib {
    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,"E:\\study\\java\\cgliblearn\\src\\main\\java\\enhancer");
                //czy.testcglib s=new czy.testcglib();
        System.out.println(CallbackFilter.class.getClassLoader());
        System.out.println(Callback.class.getInterfaces().length==0);
        System.out.println(InvocationHandler.class.getInterfaces()[0]);
        Enhancer enhancer= new Enhancer();
        enhancer.setCallback(new PrintCallBack());

        //enhancer.setCallbackType(czy.PrintCallBack.class);
        enhancer.setSuperclass(TestClassCGlib.class);
        enhancer.setUseFactory(false);
        TestClassCGlib test=(TestClassCGlib) enhancer.create();
        System.out.println("查看生成子类");

        //实际上是访问生成子类的toString方法和其他方法，
        // 生成类被cglib修改了，所以会报错
        // System.out.println( test);
//        System.out.println(test.CGLIB$toString$2());
        System.out.println("结束查看生成子类");
        String hh= test.print("czy.testcglib");
        System.out.println(hh);
        System.out.println(czy.YYY.ordinal());

    }

    /**
     * static方法不能实现多态，
     * 所以cglib代理后，使用父类实例还是只能调用父类方法
     * @param str
     * @return
     */

    public static String printStatic(String str){
        System.out.println("原始函数执行："+str);
        return "456";
    }

    public  String print(String str){
        System.out.println("原始函数执行："+str);
        return "456";
    }


}
class PrintCallBack implements InvocationHandler {
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

/**
 * 拦截器执行的返回结果必须和原方法的类型兼容，即子类。
 */
class PrintInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("czy.PrintInterceptor 在方法执行前的切面");
        System.out.println(obj.getClass().getSuperclass());
//        System.out.println(obj.toString()); // 会导致递归
// 在生成实现方法里面自己调用obj会出现stackoverflow是为什么？？？？
        Object o=proxy.invokeSuper(obj,args);
        System.out.println("czy.PrintInterceptor 在方法执行后的切面");
        return method.getName();
    }
}
enum czy{
    XXX,
    YYY;

    czy() {
    }
}

//final class abc extends Enum<abc>{ // 不能直接继承Enum
//
//    /**
//     * Sole constructor.  Programmers cannot invoke this constructor.
//     * It is for use by code emitted by the compiler in response to
//     * enum type declarations.
//     *
//     * @param name    - The name of this enum constant, which is the identifier
//     *                used to declare it.
//     * @param ordinal - The ordinal of this enumeration constant (its position
//     *                in the enum declaration, where the initial constant is assigned
//     */
//    protected abc(String name, int ordinal) {
//        super(name, ordinal);
//    }
//}


//class czy.PrintCallBack implements NoOp {
//}

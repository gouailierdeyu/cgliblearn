package czy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/29 23:51
 *
 * @version 1.0
 */
public class TestInterfacceCGlib {
    public static void main(String[] args) {
        Enhancer enhancer= new Enhancer();
        enhancer.setCallback(new ResultCallBack());

        //enhancer.setCallbackType(czy.PrintCallBack.class);
        enhancer.setSuperclass(Object.class);
        enhancer.setInterfaces(new Class[]{iproxyface.class});
        enhancer.setUseFactory(false);
        iproxyface test= (iproxyface)enhancer.create();
        System.out.println("查看生成子类");

        //实际上是访问生成子类的toString方法和其他方法，
        // 生成类被cglib修改了，所以会报错
        // System.out.println( test);
//        System.out.println(test.CGLIB$toString$2());
        System.out.println("结束查看生成子类");
        String hh= test.getresult();
        System.out.println(hh);
    }
}

interface iproxyface{

    String getresult();
}
class ResultCallBack implements InvocationHandler {
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

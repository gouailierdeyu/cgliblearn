package cn.canerme.httpproxy;

import net.sf.cglib.proxy.*;

import java.lang.reflect.Method;
import java.util.*;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/28 19:16
 *
 * @version 1.0
 */

/**
 * 建议继承过滤器实现自己的回调过滤时，
 * 直接实现CallbackHelper的Object getCallback(Method method)方法。
 */
public class HttpProxyFilter extends CallbackHelper {

    private static final PostMethodProxy postMethodProxy= new PostMethodProxy();
    private static final GetMethodProxy getMethodProxy= new GetMethodProxy();

    public HttpProxyFilter(Class superclass,Class[] interfaces) {
        super(superclass, interfaces);
    }

    @Override //这里要去重，用单例
    protected Object getCallback(Method method) {
        if (method.getName().contains("post")){
            return postMethodProxy;
        }else if (method.getName().contains("get")){
            return getMethodProxy;
        }
//        System.out.println(method);
        return (NoOp)(NoOp.INSTANCE);
    }
}

package cn.canerme.httpproxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/30 22:06
 *
 * @version 1.0
 */
public class PutMethodProxy implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return null;
    }
}

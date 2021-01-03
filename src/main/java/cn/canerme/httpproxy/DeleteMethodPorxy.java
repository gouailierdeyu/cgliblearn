package cn.canerme.httpproxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/30 22:07
 *
 * @version 1.0
 */
public class DeleteMethodPorxy implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("PostMethodProxy");

        return null;
    }
}

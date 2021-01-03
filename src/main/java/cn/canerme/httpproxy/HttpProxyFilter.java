package cn.canerme.httpproxy;

import cn.canerme.exception.HttpMethodConfusionException;
import cn.canerme.httpmethod.annotation.DELETE;
import cn.canerme.httpmethod.annotation.GET;
import cn.canerme.httpmethod.annotation.POST;
import cn.canerme.httpmethod.annotation.PUT;
import net.sf.cglib.proxy.*;

import java.lang.annotation.Annotation;
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

    private static  PostMethodProxy postMethodProxy= new PostMethodProxy();
    private static  GetMethodProxy getMethodProxy= new GetMethodProxy();
    private static  DeleteMethodPorxy deleteMethodPorxy = new DeleteMethodPorxy();
    private static PutMethodProxy putMethodProxy = new PutMethodProxy();


    public HttpProxyFilter(Class superclass,Class[] interfaces) {
        super(superclass, interfaces);
    }

    @Override
    protected Object getCallback(Method method)  {
        if (method.getName().contains("post") ){
            return postMethodProxy;
        }else if (method.getName().contains("get")){
            return getMethodProxy;
        }

        int flag = 0;
        Annotation[] methodAnnotations = method.getAnnotations();
        for (int i = 0; i < methodAnnotations.length; i++) {
            if (methodAnnotations[i].toString().contains("GET")||
                    methodAnnotations[i].toString().contains("POST")||
                    methodAnnotations[i].toString().contains("PUT")||
                    methodAnnotations[i].toString().contains("DELETE")){
                flag++;
            }
        }

        if (flag==2) {
                throw  new HttpMethodConfusionException(method.getName()+"() can proxy only one http method");
        }
//        if (flag==0)
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation instanceof GET) {
                return getMethodProxy;
            }else if (annotation instanceof POST){
                return postMethodProxy;
            }else if (annotation instanceof PUT){
                return putMethodProxy;
            }else if (annotation instanceof DELETE){
                return deleteMethodPorxy;
            }
        }
//        System.out.println(method);
        return (NoOp)(NoOp.INSTANCE);
    }
}

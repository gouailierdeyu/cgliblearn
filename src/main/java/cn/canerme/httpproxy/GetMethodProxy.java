package cn.canerme.httpproxy;

import cn.canerme.httpmethod.GET;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/28 11:01
 *
 * @version 1.0
 */
public class GetMethodProxy implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String uri=null;String query = null;String [] headers=null;
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof GET) {
                uri = ((GET) annotation).uri();
                query = ((GET) annotation).query();
                headers =((GET) annotation).headers();
                System.out.println(((GET) annotation).uri());
                return doGet(uri,query,headers);
            }
        }

        if (args[0]!=null)
            uri= (String) args[1];
        if (args[1]!=null)
            query = (String) args[1];
        if (args[2]!=null)
            headers = (String[]) args[2];
        return doGet(uri,query,headers);
    }

    public String doGet(String uri,String query,String [] headers ) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(uri)).build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return httpResponse.body();
    }
}

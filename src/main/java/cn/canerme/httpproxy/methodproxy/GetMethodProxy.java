package cn.canerme.httpproxy.methodproxy;

import cn.canerme.httpproxy.httpmethod.annotation.GET;
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
        String uri=null;String[] query = null;String [] headers=null;
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof GET) {
                uri = ((GET) annotation).uri();
                query = ((GET) annotation).query();
                headers =((GET) annotation).headers();
//                System.out.println(((GET) annotation).uri());
                return doGet(uri,query,headers);
            }
        }

        if (args[0]!=null)
            uri= (String) args[0];
        if (args[1]!=null )
            query = (String[]) args[1];
        if (args[2]!=null)
            headers = (String[]) args[2];
        return doGet(uri,query,headers);
    }

    /**
     * HttpClient.send()源码发现这个函数内部还是调用了异步的函数sendAsync()方法，然后get()出结果
     * @param uri 资源路径
     * @param query 查询参数
     * @param headers 请求头参数
     * @return 响应体
     * @throws IOException 网络IO异常
     * @throws InterruptedException 中断异常
     */
    public String doGet(String uri,String[] query,String [] headers ) throws IOException, InterruptedException {
        if(query!=null && query.length!=0){
            if (query.length%2!=0){
                throw new IllegalArgumentException("http query must be key1=value1&key2=value2, so the query length must be even.");
            }
            StringBuilder q= new StringBuilder(query[0]);
            for (int i = 1; i < query.length; i++) {
                if (i%2==0){
                    q.append("&").append(query[i]);
                }else {
                    q.append("=").append(query[i]);
                }

            }
            if (uri.endsWith("?"))
                uri = uri+q;
            else
                uri = uri+"?"+q;
        }
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest.Builder builder;
//        System.out.println(headers.length);
        if (headers!=null && headers.length!=0) {
            if (headers.length%2!=0){
                throw new IllegalArgumentException("http headers must be key1=value1&key2=value2, so the headers length must be even.");
            }
            builder = HttpRequest.newBuilder(URI.create(uri)).GET().headers(headers);
        } else {
            builder = HttpRequest.newBuilder(URI.create(uri)).GET();
        }
        HttpResponse<String> httpResponse = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return httpResponse.body();
    }
}

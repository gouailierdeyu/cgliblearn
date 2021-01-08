package cn.canerme;

import cn.canerme.httpclient.HttpClient;
import cn.canerme.httpproxy.GetMethodProxy;
import cn.canerme.httpproxy.HttpProxyFilter;
import cn.canerme.httpproxy.PostMethodProxy;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.beans.EventHandler;
import java.util.Arrays;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/28 11:13
 *
 * @version 1.0
 */
public class HttpFactory {

    public static HttpClient httpClient = null;
    private HttpFactory() {
    }

    public static HttpClient newHttpClient(){
        HttpProxyFilter httpProxyFilter = new HttpProxyFilter(Object.class,new Class[]{HttpClient.class});
        Enhancer enhancer =new Enhancer();
        enhancer.setUseFactory(false);
        enhancer.setSuperclass(Object.class);
        enhancer.setInterfaces(new Class[]{HttpClient.class});
        enhancer.setCallbackFilter(httpProxyFilter);
        enhancer.setCallbacks(httpProxyFilter.getCallbacks());
        httpClient =(HttpClient)enhancer.create();
        return httpClient;
    }

    public static synchronized HttpClient getHttpClient(){
        if (httpClient==null) {
            httpClient = newHttpClient();
        }
        return httpClient;
    }

//    public static synchronized HttpClient getHttpClient(Class clientInterface){
//        if (httpClient==null) {
//            new HttpFactory(clientInterface);
//        }
//        return httpClient;
//    }

    public static synchronized <T> T getHttpClient(Class<T> clientInterface){
//        System.out.println(clientInterface.getGenericSuperclass());
        HttpProxyFilter httpProxyFilter = new HttpProxyFilter(Object.class,new Class[]{clientInterface});
        Enhancer enhancer =new Enhancer();
        enhancer.setUseFactory(false);
        enhancer.setSuperclass(Object.class);
        enhancer.setInterfaces(new Class[]{clientInterface});
        enhancer.setCallbackFilter(httpProxyFilter);
        enhancer.setCallbacks(httpProxyFilter.getCallbacks());
        T httpClient=(T)enhancer.create();
        return httpClient;
//        if (httpClient==null) {
//            new HttpFactory(clientInterface);
//        }
//        return httpClient;
    }

    public static void main(String[] args) throws Throwable {
//        HttpProxyFilter httpProxyFilter = new HttpProxyFilter(HttpClient.class);
//        Arrays.stream(httpProxyFilter.getCallbacks()).forEach(System.out::println);
        HttpClient httpClient = HttpFactory.getHttpClient();
        String s = httpClient.get("S", new String[]{"s","a"}, null);
        System.out.println(s);
        httpClient.post(null);
    }
}

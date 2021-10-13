package cn.canerme.httpproxy;

import cn.canerme.httpproxy.httpclient.HttpClient;
import cn.canerme.httpproxy.methodproxy.HttpProxyFilter;
import net.sf.cglib.proxy.Enhancer;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/28 11:13
 *
 * @version 1.0
 */
public final class HttpClientFactory {

    public static HttpClient httpClient = null;
    private HttpClientFactory() {
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

    @SuppressWarnings("unchecked")
    public static synchronized <T> T getHttpClient(Class<T> clientInterface){
//        System.out.println(clientInterface.getGenericSuperclass());
        HttpProxyFilter httpProxyFilter = new HttpProxyFilter(Object.class,new Class[]{clientInterface});
        Enhancer enhancer =new Enhancer();
        enhancer.setUseFactory(false);
        enhancer.setSuperclass(Object.class);
        enhancer.setInterfaces(new Class[]{clientInterface});
        enhancer.setCallbackFilter(httpProxyFilter);
        enhancer.setCallbacks(httpProxyFilter.getCallbacks());
        return (T)enhancer.create();

    }
}

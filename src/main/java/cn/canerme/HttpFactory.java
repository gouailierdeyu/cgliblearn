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
        HttpProxyFilter httpProxyFilter = new HttpProxyFilter(HttpClient.class);
        Enhancer enhancer =new Enhancer();
        enhancer.setUseFactory(false);
        enhancer.setSuperclass(HttpClient.class);
        enhancer.setCallbackFilter(httpProxyFilter);
        enhancer.setCallbacks(httpProxyFilter.getCallbacks());
        httpClient =(HttpClient)enhancer.create();
    }

    public static synchronized HttpClient getHttpClient(){
        if (httpClient==null) {
            new HttpFactory();
        }
        return httpClient;
    }


    public static void main(String[] args) {
//        HttpProxyFilter httpProxyFilter = new HttpProxyFilter(HttpClient.class);
//        Arrays.stream(httpProxyFilter.getCallbacks()).forEach(System.out::println);
        HttpClient httpClient = HttpFactory.getHttpClient();
        httpClient.get("s","s",null);
        httpClient.post(null);
    }
}

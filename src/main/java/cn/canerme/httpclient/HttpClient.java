package cn.canerme.httpclient;

import cn.canerme.httpmethod.GET;

import java.util.Map;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/28 14:59
 *
 * @version 1.0
 */
abstract public class HttpClient {

    // post组

    public abstract String post(String uri, String query, Map<String, String> headers) throws Throwable;
    public abstract String post(String uri, String query) throws Throwable;
    public abstract String post(String uri) throws Throwable;

    // get组
    @GET(uri = "http://www.baidu.com")
    public abstract String get(String uri, String query, Map<String, String> headers) throws Throwable;
    public abstract String get(String uri, String query) throws Throwable;
    public abstract String get(String uri) throws Throwable;
}

package cn.canerme.httpclient;

import java.util.Map;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/28 14:59
 *
 * @version 1.0
 */
abstract public class HttpClient {

    // post组
    public abstract String post(String uri, String query, Map<String, String> headers);
    public abstract String post(String uri, String query);
    public abstract String post(String uri);

    // get组
    public abstract String get(String uri, String query, Map<String, String> headers);
    public abstract String get(String uri, String query);
    public abstract String get(String uri);
}

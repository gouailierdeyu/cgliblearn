package cn.canerme.httpproxy.httpclient;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/28 14:59
 *
 * @version 1.0
 */
public interface HttpClient {

    // post组 用于发起新增请求
    String post(String uri, Object query, String[] headers) throws Throwable;
    String post(String uri, Object query) throws Throwable;
    String post(String uri) throws Throwable;

    String post(String uri, String[] query, String[] headers) throws Throwable;
    String post(String uri, String[] query) throws Throwable;

    // get组 用于发起查询请求
    String get(String uri, String[] query, String[] headers) throws Throwable;
    String get(String uri, String[] query) throws Throwable;
    String get(String uri) throws Throwable;

    // put组 用于发起更改请求
    String put(String uri, Object query, String[] headers) throws Throwable;
    String put(String uri, Object query) throws Throwable;
    String put(String uri) throws Throwable;

    // delete组 用于发起删除请求
    String delete(String uri, String query, String[] headers) throws Throwable;
    String delete(String uri, String query) throws Throwable;
    String delete(String uri) throws Throwable;
}

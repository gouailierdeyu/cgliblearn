package cn.canerme.exception;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/30 21:34
 *
 * @version 1.0
 */
public class HttpMethodConfusionException extends  RuntimeException{
    public HttpMethodConfusionException(String s) {
        super(s);
    }
}

package cn.canerme.test;

import cn.canerme.httpproxy.httpmethod.annotation.GET;
import cn.canerme.httpproxy.httpmethod.annotation.POST;
import cn.canerme.httpproxy.httpmethod.annotation.RequestBody;

import java.util.Map;

/**
 * UTF-8
 * Created by czy  Time : 2021/1/9 14:56
 *
 * @version 1.0
 */
public interface Imymsg{
    @GET(uri = "http://www.baidu.com")
    String search();

    @POST(uri = "")
    String post(@RequestBody Map s);
}

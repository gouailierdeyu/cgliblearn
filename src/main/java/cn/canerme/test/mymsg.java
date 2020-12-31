package cn.canerme.test;

import cn.canerme.httpmethod.annotation.GET;
import cn.canerme.httpmethod.annotation.POST;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/30 21:15
 *
 * @version 1.0
 */
public interface mymsg {
    @GET(uri = "http://www.baidu.com")

    String search();
}

package cn.canerme.test;

import cn.canerme.HttpFactory;
import cn.canerme.httpclient.HttpClient;

import java.util.concurrent.ConcurrentHashMap;


/**
 * UTF-8
 * Created by czy  Time : 2020/12/30 21:15
 *
 * @version 1.0
 */

public class TestzidingyiInterface {


  public void sendmsg(){
    mymsg httpClient = HttpFactory.getHttpClient( mymsg.class);
    System.out.println(httpClient.search());

  }
}

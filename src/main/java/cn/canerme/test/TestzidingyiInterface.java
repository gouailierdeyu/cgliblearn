package cn.canerme.test;

import cn.canerme.HttpFactory;
import cn.canerme.httpclient.HttpClient;
import org.junit.jupiter.api.Test;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/30 21:15
 *
 * @version 1.0
 */

public class TestzidingyiInterface {
  @Test
  public void sendmsg(){
    mymsg httpClient = HttpFactory.getHttpClient( mymsg.class);
    System.out.println(httpClient.search());
  }
}

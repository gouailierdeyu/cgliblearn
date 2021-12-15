package cn.canerme.httpproxy.example;

import cn.canerme.httpproxy.HttpClientFactory;
import cn.canerme.httpproxy.httpclient.HttpClient;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * UTF-8
 * Created by czy  Time : 2021/10/13 10:58
 * --add-opens java.base/java.lang=ALL-UNNAMED
 * @version 1.0
 */
public class ScuNcovReport {
    public static void main(String[] args) throws Throwable {
        LocalTime time = LocalTime.of(9,30);
        long period = 60*60*24;
        ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(2);
        System.out.println(LocalTime.now().until(time, ChronoUnit.SECONDS));
        scheduled.scheduleAtFixedRate(()->{
            try {
                doSaveNcov();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        },LocalTime.now().until(time, ChronoUnit.SECONDS), period, TimeUnit.SECONDS);

    }


    public static void doSaveNcov() throws Throwable{
        HttpClient httpClient = HttpClientFactory.getHttpClient();
        List<String> headers = new ArrayList<>();
        headers.add("X-Requested-With");
        headers.add("XMLHttpRequest");
        headers.add("User-Agent");
        headers.add("Mozilla/5.0 (Linux; Android 11; RMX2072 Build/RKQ1.200710.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/86.0.4240.99 XWEB/3135 MMWEBSDK/20210902 Mobile Safari/537.36 MMWEBID/862 MicroMessenger/8.0.15.2020(0x28000F35) Process/toolsmp WeChat/arm64 Weixin NetType/WIFI Language/zh_CN ABI/arm64");
        headers.add("Content-Type");
        headers.add("application/x-www-form-urlencoded; charset=UTF-8");
        headers.add("Origin");
        headers.add("https://wfw.scu.edu.cn");
        headers.add("Accept-Language");
        headers.add("zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.add("Referer");
        headers.add("https://wfw.scu.edu.cn/ncov/wap/default/index");
        headers.add("Cookie");
        headers.add("eai-sess=hdqqufnptvfnucfclh9d8dk8i4; UUkey=82b6b2ce86aab98933657fbaf0e555db; Hm_lvt_48b682d4885d22a90111e46b972e3268=1633051048,1633058291,1633577651; Hm_lpvt_48b682d4885d22a90111e46b972e3268=1633744312");

        String postResult = httpClient.post("https://wfw.scu.edu.cn/ncov/wap/default/save", body, headers.toArray(new String[0]));
        System.out.println(postResult);
    }
}

package cn.canerme.httpproxy.example.scuncov;

import cn.canerme.httpproxy.HttpClientFactory;
import cn.canerme.httpproxy.httpclient.HttpClient;
import cn.canerme.httpproxy.util.UicodeBackslashU;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * UTF-8
 * Created by czy  Time : 2021/10/13 10:58
 * --add-opens java.base/java.lang=ALL-UNNAMED
 * @version 1.0
 */
public class ScuNcovReport {
    public static HttpClient httpClient = HttpClientFactory.getHttpClient();
    public static void main(String[] args) throws Throwable {
        doLOLRank();
//        LocalTime time = LocalTime.of(9,15);
//        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        long period = 60*60*24;
//        long iniTime;
//        if (LocalTime.now().until(time,ChronoUnit.SECONDS)<0){
//            iniTime = period+LocalTime.now().until(time,ChronoUnit.SECONDS);
//        }else {
//            iniTime = LocalTime.now().until(time,ChronoUnit.SECONDS);
//        }
//
//        scheduledExecutorService.scheduleAtFixedRate(()->{
//            try {
//                doSaveNcov();
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        },iniTime, period, TimeUnit.SECONDS);
//        System.out.println(iniTime);

    }


    public static void doSaveNcov() throws Throwable{

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
        doPushMessage(postResult);
    }

    public static void doPushMessage(String postResult) throws Throwable {
        String title = URLEncoder.encode("微服务健康日报",StandardCharsets.UTF_8);
        ServerChanMessage serverChanMessage = new ServerChanMessage(title, URLEncoder.encode(postResult,StandardCharsets.UTF_8));
        String pushResult = httpClient.post(messageUrl+"?"+serverChanMessage.toString());
        System.out.println(UicodeBackslashU.unicodeToCn(pushResult));
    }


    public static void doLOLRank() throws Throwable{

        List<String> headers = new ArrayList<>();
        headers.add("Accept");
        headers.add("text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        headers.add("User-Agent");
        headers.add("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.124 Safari/537.36 qblink wegame.exe WeGame/5.1.3.12231 QBCore/3.70.107.400 QQBrowser/9.0.2524.400");
        headers.add("Referer");
        headers.add("https://www.wegame.com.cn/helper/lol/discovery/user_rank.html?navid=42");
        headers.add("Accept-Encoding");
        headers.add("gzip, deflate, br");
        headers.add("Accept-Language");
        headers.add("zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.5;q=0.7");
        headers.add("Cookie");
        List<String> query = new ArrayList<>();
        query.add("callback");
        query.add("jQuery22209805767732218054_1641871822033");
        query.add("rank_name");
        query.add("solo");
        query.add("area_id");
        query.add("14");
        query.add("offset");
        query.add("10");
        query.add("limit");
        query.add("10");
        query.add("_");
        query.add(String.valueOf(System.currentTimeMillis()));
        String getResult = httpClient.get("https://www.wegame.com.cn/api/pallas/lol/ranks/get_tier_rank", query.toArray(new String[0]), headers.toArray(new String[0]));
        System.out.println(getResult);

    }
}

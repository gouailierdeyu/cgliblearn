package cn.canerme.httpproxy.httprequest;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Objects;

/**
 * UTF-8
 * Created by czy  Time : 2022/1/3 15:47
 *
 * @version 1.0
 */
public class RequestUtil {
    public static HttpRequest getRequest(String uri, String postBody, String... headers){
        if (Objects.isNull(headers)){
            return HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .POST(HttpRequest.BodyPublishers.ofString(postBody))
                    .build();
        }else {
            return HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .headers(headers)
                    .POST(HttpRequest.BodyPublishers.ofString(postBody))
                    .build();
        }
    }
}

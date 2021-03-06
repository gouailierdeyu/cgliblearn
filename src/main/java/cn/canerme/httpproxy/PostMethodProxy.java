package cn.canerme.httpproxy;

import cn.canerme.httpmethod.annotation.POST;
import cn.canerme.httpmethod.annotation.RequestBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/28 11:01
 *
 * @version 1.0
 */
public class PostMethodProxy implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String uri=null;String [] headers=null;
        boolean json=false;
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof POST) {
                String body =null;
                uri = ((POST) annotation).uri();
                headers =((POST) annotation).headers();
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Annotation[] declaredAnnotations = parameters[i].getDeclaredAnnotations();
                    for (Annotation declaredAnnotation : declaredAnnotations) {
                        if (declaredAnnotation instanceof RequestBody){
                            ObjectMapper objectMapper = new ObjectMapper();
                            body = objectMapper.writeValueAsString(args[i]);
                            json=true;
                            return doPost(uri,body,headers,json);
                        }
                    }
                }

                return doPost(uri,args[0],headers,json);
//                System.out.println(((GET) annotation).uri());

            }
        }
        String[] body = new String[0];
        Object jsonbody=null;
        if (annotations.length==0){
            if (args[0]!=null)
                uri= (String) args[0];
            if (args.length>1 && args[1]!=null && args[1] instanceof String[])
                body = (String[]) args[1];
            else if(args.length > 1 && args[1] != null){
                jsonbody = args[1];
                json=true;
            }
            if (args.length>2 && args[2]!=null)
                headers = (String[]) args[2];

        }
        if (json)
            doPost(uri,jsonbody,headers,json);
        else
            doPost(uri,body,headers,json);

        // System.out.println("PostMethodProxy");
        return doPost(uri,body,headers,json);
    }

    private int doPost(String uri, Object body, String[] headers, boolean json) throws IOException, InterruptedException {
        HttpRequest request = null;
        String bo = null;
        for (int i = 0; i < headers.length; i++) {
            if ("content-type".equalsIgnoreCase(headers[i]) ) {
                if ("application/json".equalsIgnoreCase(headers[i+1]) ){
                    json=true;
                }
                if (json){
                    headers[i+1]="application/json";
                    request= HttpRequest.newBuilder()
                            .uri(URI.create(uri))
                            .headers(headers)
                            .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(body)))
                            .build();
                }

            }
        }
        if (!json){
            String [] query = (String[]) body;
            if(query!=null && query.length!=0){
                if (query.length%2!=0){
                    throw new IllegalArgumentException("http query must be key1=value1&key2=value2, so the query length must be even.");
                }
                bo= query[0];
                for (int i = 1; i < query.length; i++) {
                    if (i%2==0){
                        bo+="&"+query[i];
                    }else {
                        bo+="="+query[i];
                    }
                }
            }
            request= HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .headers(headers)
                    .POST(HttpRequest.BodyPublishers.ofString(bo))
                    .build();

        }

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode();
    }
}

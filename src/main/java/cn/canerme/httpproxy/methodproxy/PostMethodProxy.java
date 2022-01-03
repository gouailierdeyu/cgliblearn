package cn.canerme.httpproxy.methodproxy;

import cn.canerme.httpproxy.httpmethod.annotation.POST;
import cn.canerme.httpproxy.httpmethod.annotation.RequestBody;
import cn.canerme.httpproxy.httprequest.RequestUtil;
import cn.canerme.httpproxy.util.UicodeBackslashU;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.tools.ant.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/28 11:01
 *
 * @version 1.0
 */
public class PostMethodProxy implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String uri=null;
        String [] headers=null;
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
                            json=true;
                            return doPost(uri,args[i],headers,json);
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
            return doPost(uri,jsonbody,headers,json);
        else
            return doPost(uri,body,headers,json);

        // System.out.println("PostMethodProxy");
    }

    private String doPost(String uri, Object body, String[] headers, boolean json) throws IOException, InterruptedException {
        HttpRequest request = null;
        String bo = "";
        if (!Objects.isNull(headers))
        for (int i = 0; i < headers.length; i++) {
            if ("content-type".equalsIgnoreCase(headers[i]) ) {
                if (i+1<headers.length && headers[i+1].toLowerCase().contains("application/json") ){
                    json=true;
                    break;
                }
            }
        }
        if (json){
            ObjectMapper objectMapper = new ObjectMapper();
            String postBody = objectMapper.writeValueAsString(body);
            request = RequestUtil.getRequest(uri,postBody,headers);
        }else {
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

            request = RequestUtil.getRequest(uri,bo,headers);

        }

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return UicodeBackslashU.unicodeToCn(response.body());
    }
}

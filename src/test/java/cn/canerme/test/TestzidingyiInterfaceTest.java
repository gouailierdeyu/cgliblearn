package cn.canerme.test;

import cn.canerme.httpproxy.HttpClientFactory;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * UTF-8
 * Created by czy  Time : 2021/1/9 14:56
 *
 * @version 1.0
 */
public class TestzidingyiInterfaceTest {

    @Test
    public void sendmsg(){
        Imymsg httpClient = HttpClientFactory.getHttpClient(Imymsg.class);
        System.out.println(httpClient.search());
//        StringBuilder
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String s="abcd";
        String s16="abcd中国";
        Method coder = String.class.getDeclaredMethod("isLatin1");
        coder.setAccessible(true);

        System.out.println(coder.invoke(s));
        System.out.println(coder.invoke(s16));
    }
}

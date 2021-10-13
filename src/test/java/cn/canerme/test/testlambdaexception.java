package cn.canerme.test;

import net.sf.cglib.core.ClassNameReader;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * UTF-8
 * Created by czy  Time : 2021/1/12 10:37
 *
 * @version 1.0
 */
public class testlambdaexception {
    @Test
    public void testouterunchecklambdaexception(){
        // 非受检异常可以在最外部处理
        List<Integer> integers = Arrays.asList(3, 0, 7, 6, 10, 20);
        try {
            integers.forEach(i -> {
                System.out.println(50 / i);
                throw EARLY_EXIT;
            });
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }
    private static final EarlyExitException EARLY_EXIT = new EarlyExitException();
    private static class EarlyExitException extends RuntimeException { }

    @Test
    public void testouterchecklambdaexception(){
        List<Integer> integers = Arrays.asList(3, 5, 7, 6, 10, 20);
        // lambda表达式中受检异常必须在体内处理
            integers.forEach(i -> {
                System.out.println(50 / i);
                try {
                    throw new IOException();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            });
    }

    @Test
    public void testClass(){
        Object s=new String("sds");
        System.out.println(s.getClass());
    }

}

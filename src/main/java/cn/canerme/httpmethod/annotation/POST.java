package cn.canerme.httpmethod.annotation;

import net.sf.cglib.reflect.MulticastDelegate;

import java.lang.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/28 11:07
 *
 * @version 1.0
 */

/**
 * post传递参数有三种可能
 * 1.传递string字符串查询
 * 2.直接传递Object转json
 * 3.传递一个map，转json
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface POST {
    String uri() default  "";
    String[] headers() default {};
}

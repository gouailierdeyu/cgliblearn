package cn.canerme.httpproxy.httpmethod.annotation;

import java.lang.annotation.*;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/30 21:38
 *
 * @version 1.0
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PUT {
    String uri() default  "";
    String query() default "";
    String[] headers() default {};
}

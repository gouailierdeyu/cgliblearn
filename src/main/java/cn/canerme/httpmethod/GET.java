package cn.canerme.httpmethod;

import java.lang.annotation.*;

/**
 * UTF-8
 * Created by czy  Time : 2020/12/28 11:06
 *
 * @version 1.0
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GET {
   String value();
   String uri();
   String query();
}

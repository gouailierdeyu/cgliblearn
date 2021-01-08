package cn.canerme.httpmethod.annotation;

import java.lang.annotation.*;

/**
 * UTF-8
 * Created by czy  Time : 2021/1/8 11:36
 *
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RequestBody {
}

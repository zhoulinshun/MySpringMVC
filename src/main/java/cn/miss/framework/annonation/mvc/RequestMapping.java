package cn.miss.framework.annonation.mvc;

import cn.miss.framework.mvc.request.HttpRequestMethod;

import java.lang.annotation.*;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/9/20.
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";

    HttpRequestMethod[] method() default {};

//    String[] params() default {};
//
//    String[] headers() default {};
//
//    String[] consumes() default {};
//
//    String[] produces() default {};
}

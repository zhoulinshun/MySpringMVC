package cn.miss.framework.annonation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/12.
 */
@Target({TYPE, FIELD, METHOD,PARAMETER})
@Retention(RUNTIME)
public @interface Param {
    String name() default "";
    Class type() default java.lang.Object.class;
}

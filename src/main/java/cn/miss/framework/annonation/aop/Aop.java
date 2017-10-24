package cn.miss.framework.annonation.aop;

import java.lang.annotation.*;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/23.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aop {
    String[] aspect();

    boolean after() default true;

    boolean before() default true;
}

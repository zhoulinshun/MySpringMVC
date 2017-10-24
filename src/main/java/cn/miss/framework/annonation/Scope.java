package cn.miss.framework.annonation;

import java.lang.annotation.*;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/9/21.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {
    boolean singleton() default true;
}

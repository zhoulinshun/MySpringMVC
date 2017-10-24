package cn.miss.framework.annonation;

import java.lang.annotation.*;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/9/20.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}

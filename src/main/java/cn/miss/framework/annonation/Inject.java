package cn.miss.framework.annonation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author MissNull
 * @Description: 依赖注入
 * @Date: Created in 2017/10/13.
 */
@Target({TYPE, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface Inject {
    String name() default "";

    Class<?> type() default Object.class;
}

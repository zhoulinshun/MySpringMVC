package cn.miss.framework.annonation;

import java.lang.annotation.*;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/12.
 */
@Target({ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AConstructor {
}

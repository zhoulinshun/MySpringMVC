package cn.miss.framework.annonation.aop;

import java.lang.annotation.*;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/23.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Before {
}

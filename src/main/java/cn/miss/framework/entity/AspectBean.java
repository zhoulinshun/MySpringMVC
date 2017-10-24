package cn.miss.framework.entity;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/23.
 */
public class AspectBean {
    private List<Method> before;
    private List<Method> after;
    private Class clazz;
    private String aspectName;

    public AspectBean(List<Method> before, List<Method> after, Class clazz, String aspectName) {
        this.before = before;
        this.after = after;
        this.clazz = clazz;
        this.aspectName = aspectName;
    }

    public List<Method> getBefore() {
        return before;
    }

    public List<Method> getAfter() {
        return after;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getAspectName() {
        return aspectName;
    }
}

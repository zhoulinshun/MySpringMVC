package cn.miss.framework.bean;

import java.lang.reflect.Method;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/23.
 */
public class JoinPoint {
    private Method method;
    private Class clazz;
    private Object[] args;
    private Object object;

    public JoinPoint( Method method, Class clazz, Object[] args, Object object) {
        this.method = method;
        this.clazz = clazz;
        this.args = args;
        this.object = object;
    }


    public Method getMethod() {
        return method;
    }

    public Class getClazz() {
        return clazz;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getObject() {
        return object;
    }
}

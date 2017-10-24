package cn.miss.framework.aop;

import cn.miss.framework.annonation.aop.Aop;
import cn.miss.framework.bean.BeanFactory;
import cn.miss.framework.bean.JoinPoint;
import cn.miss.framework.entity.AspectBean;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/22.
 */
public class ObjectProxy {
    private Enhancer enhancer;
    private BeanFactory beanFactory;
    private Map<String, AspectBean> aspectBeanMap;


    public ObjectProxy(BeanFactory beanFactory, Map<String, AspectBean> aspectBeanMap) {
        this.beanFactory = beanFactory;
        this.aspectBeanMap = aspectBeanMap;
        init();
    }

    public ObjectProxy() {
        this(null, null);
    }

    public void init() {
        enhancer = new Enhancer();
    }

    public Object proxy(Object o) {
        enhancer.setSuperclass(o.getClass());
        enhancer.setCallback(new MyIntercept(o));
        return enhancer.create();
    }


    private class MyIntercept implements MethodInterceptor {
        public Object object;


        public MyIntercept(Object o) {
            this.object = o;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            JoinPoint joinPoint = getJoinPoint(objects, method, objects);
            before(joinPoint);
            Object result = method.invoke(object, objects);
            after(joinPoint);
            return result;
        }

        private JoinPoint getJoinPoint(Object o, Method method, Object[] objects) {
            return new JoinPoint(method, o.getClass(), objects, o);
        }

        public void before(JoinPoint joinPoint) {
            Class<?> aClass = object.getClass();
            Aop annotation = aClass.getAnnotation(Aop.class);


        }

        public void after(JoinPoint joinPoint) {

        }

    }
}

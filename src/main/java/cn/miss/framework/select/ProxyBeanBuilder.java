package cn.miss.framework.select;

import cn.miss.framework.annonation.aop.After;
import cn.miss.framework.annonation.aop.Aspect;
import cn.miss.framework.annonation.aop.Before;
import cn.miss.framework.entity.AspectBean;
import cn.miss.framework.bean.BeanCore;
import cn.miss.framework.bean.BeanFactory;
import cn.miss.framework.entity.BeanEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/23.
 */
public class ProxyBeanBuilder implements BeanBuilder<AspectBean,BeanFactory> {
    @Override
    public Map<String, AspectBean> select(BeanFactory factory) {
        Map<String, AspectBean> map = new HashMap<>();
        factory.getBeanEntities().forEach(b -> {
            List<Method> before = new ArrayList<>();
            List<Method> after = new ArrayList<>();
            Class beanClass = b.getBeanClass();
            Aspect annotation = (Aspect) beanClass.getAnnotation(Aspect.class);
            if (annotation != null) {
                Method[] methods = beanClass.getMethods();
                for (Method method : methods) {
                    if (method.getAnnotation(After.class) != null) {
                        after.add(method);
                    }
                    if (method.getAnnotation(Before.class) != null) {
                        before.add(method);
                    }
                }
                if (annotation.name().equals("")) {
                    map.put(b.getBeanName(), new AspectBean(before, after, b.getBeanClass(), b.getBeanName()));
                } else {
                    map.put(annotation.name(), new AspectBean(before, after, b.getBeanClass(), annotation.name()));
                }
            }
        });
        return map;
    }
}

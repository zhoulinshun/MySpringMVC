package cn.miss.framework.select;

import cn.miss.framework.annonation.Component;
import cn.miss.framework.annonation.Scope;
import cn.miss.framework.annonation.aop.Aop;
import cn.miss.framework.entity.AspectBean;
import cn.miss.framework.bean.BeanCore;
import cn.miss.framework.bean.BeanFactory;
import cn.miss.framework.entity.BeanEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/9.
 */
public class MVCBeanBuilder implements BeanBuilder {

    @Override
    public Map<String, BeanEntity> select(BeanCore beanCore) {
        Map<String, BeanEntity> map = new HashMap<>();
        beanCore.getClasses().forEach(aClass -> {
            String beanName;
            boolean singleton;
            boolean aop;
            Component annotation = (Component) aClass.getAnnotation(Component.class);
            beanName = annotation.value();
            if (beanName.equals("")) {
                String simpleName = aClass.getSimpleName();
                char c = Character.toLowerCase(simpleName.charAt(0));
                beanName = c + simpleName.substring(1);
            }
            Scope scope = (Scope) aClass.getAnnotation(Scope.class);
            singleton = scope == null || scope.singleton();
            aop = aClass.getAnnotation(Aop.class) != null;
            map.put(beanName, new BeanEntity(singleton, aClass, beanName, aop));
        });
        return map;
    }

    @Override
    public Map<String, AspectBean> select(BeanFactory factory) {
        return null;
    }
}

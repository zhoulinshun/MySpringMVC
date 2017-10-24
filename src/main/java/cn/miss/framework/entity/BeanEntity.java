package cn.miss.framework.entity;

import cn.miss.framework.annonation.aop.Aop;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/9.
 */
public class BeanEntity {
    private boolean singleton;
    private Class beanClass;
    private String beanName;
    private Aop aop;


    public BeanEntity(boolean singleton, Class beanClass, String beanName, Aop aop) {
        this.singleton = singleton;
        this.beanClass = beanClass;
        this.beanName = beanName;
        this.aop = aop;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public String getBeanName() {
        return beanName;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public Aop getAop() {
        return aop;
    }
}

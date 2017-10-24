package cn.miss.framework.entity;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/9.
 */
public class BeanEntity {
    private boolean singleton;
    private Class beanClass;
    private String beanName;
    private boolean aop;


    public BeanEntity(boolean singleton, Class beanClass, String beanName, boolean aop) {
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

    public boolean isAop() {
        return aop;
    }
}

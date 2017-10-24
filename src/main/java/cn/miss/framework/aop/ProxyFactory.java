package cn.miss.framework.aop;

import cn.miss.framework.bean.BeanFactory;
import cn.miss.framework.entity.AspectBean;
import cn.miss.framework.select.ProxyBeanBuilder;

import java.util.Map;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/23.
 */
public class ProxyFactory {
    private boolean isAop;
    private ObjectProxy proxy;
    private BeanFactory beanFactory;
    private Map<String, AspectBean> aspectBeanEntities;

    public ProxyFactory(BeanFactory beanFactory) {
        this(beanFactory, false);
    }

    public ProxyFactory(BeanFactory beanFactory, boolean isAop) {
        this.isAop = isAop;
        this.beanFactory = beanFactory;
        init();
    }

    public void init() {
        aspectBeanEntities = beanFactory.getAspectBean(new ProxyBeanBuilder());
        proxy = new ObjectProxy(beanFactory, aspectBeanEntities);
    }

    public Object proxy(Object o) {
        if (!isAop) {
            return o;
        }
        return proxy.proxy(o);
    }

}

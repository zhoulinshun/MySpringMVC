package cn.miss.framework.select;

import cn.miss.framework.entity.AspectBean;
import cn.miss.framework.bean.BeanCore;
import cn.miss.framework.bean.BeanFactory;
import cn.miss.framework.entity.BeanEntity;

import java.util.Map;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/9.
 */

public interface BeanBuilder {
    Map<String, BeanEntity> select(BeanCore beanCore);
    Map<String, AspectBean> select(BeanFactory factory);
}

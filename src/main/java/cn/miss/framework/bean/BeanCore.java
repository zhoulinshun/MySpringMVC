package cn.miss.framework.bean;

import cn.miss.framework.Logger;
import cn.miss.framework.annonation.Component;
import cn.miss.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author MissNull
 * @Description: 负责收集指定包下的所有类
 * @Date: Created in 2017/10/8.
 */
public class BeanCore {
    private final String packageName;
    private List<Class> classes = new ArrayList<>();

    public BeanCore(String packageName) {
        this.packageName = packageName;
        init();
    }

    private void init() {
        Logger.log("BeanCore.init");
        String[] split = packageName.split(",");
        for (String s : split) {
            classes.addAll(ClassUtil.getInstancesByAnnotation2(s, Component.class));
        }
    }

    //此处不能直接返回classes
    public List<Class> getClassesByAnnotation(Class<? extends Annotation> c) {
        return classes.stream().filter(cl -> c == null || cl.getAnnotation(c) != null).collect(Collectors.toList());
    }

    public List<Class> getClasses() {
        return classes;
    }


    public String getPackageName() {
        return packageName;
    }


}

package cn.miss.framework.entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/12.
 */
public class MethodEntity {
    private Method method;
    private Map<String, Class> typeMap = new LinkedHashMap<>();
    private Map<String, Annotation> annotationMap = new LinkedHashMap<>();



}

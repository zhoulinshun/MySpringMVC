package cn.miss.framework.util;

import cn.miss.framework.exception.MethodParamException;
import cn.miss.mvc.other.MethodEntity;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/9/21.
 */
public class MethodUtil {

    private static LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    public static List<MethodEntity> getMethodByClass(Class clazz) {
        Method[] methods = clazz.getMethods();
        List<MethodEntity> list = new ArrayList<>();
        Arrays.stream(methods).forEach(method -> {
            list.add(new MethodEntity(method));
        });
        return list;
    }

    public static <T extends Annotation> Map<String, T> getMethodParamAnnotation(Class<T > c, Executable me) {
        Map<String, T> map = new LinkedHashMap<>();
        String[] parameterNames = getParameterNames(me);
        Parameter[] parameters = me.getParameters();
        for (int i = 0; i < parameterNames.length; i++) {
            T annotation = parameters[i].getAnnotation(c);
            if (annotation != null) {
                map.put(parameterNames[i], annotation);
            }
        }
        return map;
    }

    public static Map<String, Class> getMethodParam(Executable method) {
        String[] parameterNames = getParameterNames(method);
        Map<String, Class> param = new LinkedHashMap<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterNames.length != parameterTypes.length)
            throw new MethodParamException(method.getName() + "：方法参数异常");
        for (int i = 0; i < parameterNames.length; i++) {
            param.put(parameterNames[i], parameterTypes[i]);
        }
        return param;
    }

    public static String[] getParameterNames(Executable method) {
        if (method instanceof Method) {
            return discoverer.getParameterNames((Method) method);
        } else if (method instanceof Constructor) {
            return discoverer.getParameterNames((Constructor) method);
        } else {
            throw new IllegalArgumentException("不合法的参数类型");
        }
    }

}

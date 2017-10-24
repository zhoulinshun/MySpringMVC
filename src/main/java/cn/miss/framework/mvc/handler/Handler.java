package cn.miss.framework.mvc.handler;

import cn.miss.framework.Logger;
import cn.miss.framework.annonation.Param;
import cn.miss.framework.annonation.mvc.ResponseBody;
import cn.miss.framework.mvc.request.HttpRequestMethod;
import cn.miss.framework.util.MethodUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/8.
 */
public class Handler {
    private Method method;//最终执行的方法
    private String url;//访问的uri
    private Class clazz;//所在类
    private HashSet<HttpRequestMethod> supportMethod;
    private boolean returnJson;
    private Map<String, Class> typeMap = new LinkedHashMap<>();//参数集合
    private Map<String, Param> annotationMap = new LinkedHashMap<>();

    public Handler(Method method, String url, Class clazz, HttpRequestMethod[] supportMethod, boolean returnJson) {
        this.method = method;
        this.url = url;
        this.clazz = clazz;
        this.returnJson = returnJson;
        this.supportMethod = new HashSet<>();
        this.supportMethod.addAll(Arrays.asList(supportMethod));
        init();
    }

    private void init() {
        Logger.log("Handler.init");
        ResponseBody annotation = method.getAnnotation(ResponseBody.class);
        returnJson = annotation != null;
        typeMap = MethodUtil.getMethodParam(method);
        annotationMap = MethodUtil.getMethodParamAnnotation(Param.class, method);
    }

    public Object invoke(Object invoke, Object... param) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        return method.invoke(invoke, param);
    }

    public boolean isSupport(HttpRequestMethod method) {
        return supportMethod.contains(method);
    }

    public boolean isReturnJson() {
        return returnJson;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getUrl() {
        return url;
    }

    public Method getMethod() {
        return method;
    }

    public int getParamCount() {
        return typeMap.size();
    }

    public Param getAnnotationByParamName(String name) {
        return annotationMap.get(name);
    }

    public Class getTypeByParamName(String name) {
        return typeMap.get(name);
    }

    public String[] getParamNames() {
        return typeMap.keySet().toArray(new String[typeMap.keySet().size()]);
    }

    public Class[] getParamTypes() {
        return typeMap.values().toArray(new Class[typeMap.keySet().size()]);
    }

}

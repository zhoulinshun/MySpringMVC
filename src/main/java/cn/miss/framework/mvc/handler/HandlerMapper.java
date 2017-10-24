package cn.miss.framework.mvc.handler;

import cn.miss.framework.Logger;
import cn.miss.framework.annonation.mvc.RequestMapping;
import cn.miss.framework.annonation.mvc.ResponseBody;
import cn.miss.framework.bean.BeanFactory;
import cn.miss.framework.entity.BeanEntity;
import cn.miss.framework.mvc.request.HttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/8.
 */
public class HandlerMapper {
    private final Map<String, Handler> handlerMap = new ConcurrentHashMap<>();
    private BeanFactory beanFactory;

    public HandlerMapper(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        init();
    }

    private void init() {
        Logger.log("HandlerMapper.init");
        Collection<BeanEntity> beanEntity = beanFactory.getBeanEntities();
        for (BeanEntity entity : beanEntity) {
            Class clazz = entity.getBeanClass();
            String prefix = "";
            HttpRequestMethod[] requestMethods = {};
            RequestMapping annotation = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
            if (annotation != null) {
                prefix = annotation.value();
                requestMethods = annotation.method();
            }
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                String suffix;
                HttpRequestMethod[] m;
                boolean isJson;
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                if (methodAnnotation != null) {
                    suffix = methodAnnotation.value();
                    m = methodAnnotation.method();
                    String uri = uriUtil(prefix, suffix);
                    isJson = method.getAnnotation(ResponseBody.class) != null;
                    if (!handlerMap.containsKey(uri))
                        handlerMap.put(uri, new Handler(method, uri, clazz, RequestMethodUtil(requestMethods, m), isJson));
                }

            }

        }
    }

    public Handler getHandler(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith(contextPath))
            requestURI = requestURI.substring(contextPath.length());
        return handlerMap.get(requestURI);
    }


    private String uriUtil(String prefix, String suffix) {
        if (prefix.endsWith("/")) prefix = prefix.substring(0, prefix.length() - 2);
        if (suffix.startsWith("/")) suffix = suffix.substring(1);
        if (!suffix.endsWith(".action")) suffix += ".action";
        return prefix + "/" + suffix;
    }

    private HttpRequestMethod[] RequestMethodUtil(HttpRequestMethod[] clazz, HttpRequestMethod[] mMethods) {
        if (clazz.length == 0) return mMethods;
        if (mMethods.length == 0) return clazz;
        HashSet<HttpRequestMethod> hashSet = new HashSet<>();
        List<HttpRequestMethod> methods = new ArrayList<>();
        hashSet.addAll(Arrays.asList(clazz));
        for (HttpRequestMethod mMethod : mMethods) {
            if (hashSet.contains(mMethod))
                methods.add(mMethod);
        }
        return methods.toArray(new HttpRequestMethod[methods.size()]);
    }
}

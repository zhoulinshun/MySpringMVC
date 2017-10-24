package cn.miss.framework.mvc.handler;

import cn.miss.framework.annonation.Param;
import cn.miss.framework.bean.BeanFactory;
import cn.miss.framework.mvc.data.DataBinder;
import cn.miss.framework.mvc.data.MessageConvert;
import cn.miss.framework.mvc.model.Model;
import cn.miss.framework.mvc.model.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/8.
 */
public class HandlerAdapter {

    private final BeanFactory beanFactory;
    private DataBinder dataBinders;
    private List<MessageConvert> messageConverts;


    public HandlerAdapter(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ModelAndView handler(Handler handler, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = null;
        Model model = null;
        String[] paramNames = handler.getParamNames();
        Object[] objects = new Object[handler.getParamCount()];
        int i = 0;
        for (String paramName : paramNames) {
            Class aClass = handler.getTypeByParamName(paramName);
            if (aClass == HttpServletRequest.class) {
                objects[i] = request;
            } else if (aClass == HttpServletResponse.class) {
                objects[i] = response;
            } else if (aClass == Model.class) {
                model = new Model();
                objects[i] = model;
            } else if (aClass == ModelAndView.class) {
                modelAndView = new ModelAndView();
                objects[i] = modelAndView;
            } else {
                try {
                    Param annotation = handler.getAnnotationByParamName(paramName);
                    String name = paramName;
                    if (annotation != null) {
                        if (!annotation.name().equals("")) {
                            name = annotation.name();
                        }
                    }
                    if (aClass == String.class) {
                        objects[i] = request.getParameter(name);
                    } else if (aClass == String[].class) {
                        objects[i] = request.getParameterValues(name + "[]");
                    } else if (aClass == Integer.class) {
                        objects[i] = Integer.valueOf(request.getParameter(name));
                    } else if (aClass == Integer[].class) {
                        String[] parameterValues = request.getParameterValues(name + "[]");
                        Integer[] integers = new Integer[parameterValues.length];
                        objects[i] = integers;
                        for (int i1 = 0; i1 < parameterValues.length; i1++) {
                            integers[i1] = Integer.valueOf(parameterValues[i1]);
                        }
                    } else {
                        Object o = aClass.newInstance();
                        if (!(o instanceof Map || o instanceof Collection)) {
                            Field[] declaredFields = aClass.getDeclaredFields();
                            for (Field declaredField : declaredFields) {
                                declaredField.setAccessible(true);
                                String parameter = request.getParameter(declaredField.getName());
                                if (parameter != null) {
                                    declaredField.set(o, parameter);
                                }
                            }
                            objects[i] = o;
                        }
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
            i++;
        }
        try {
            Object returnValue = handler.invoke(beanFactory.getBean(handler.getClazz()), objects);
            if (returnValue == null) return modelAndView;
            if (returnValue.getClass().equals(Void.TYPE)) {
                if (modelAndView != null) {
                    if (model != null)
                        modelAndView.addAllAttrs(model.getModel());
                    return modelAndView;
                } else {
                    if (model != null) {
                        modelAndView = new ModelAndView();
                        modelAndView.addAllAttrs(model.getModel());
                    }
                    return modelAndView;
                }
            } else {
                if (modelAndView == null) modelAndView = new ModelAndView();
                if (model == null) model = new Model();
                if (handler.isReturnJson()) {
                    model.addAttr("return_json_miss", returnValue);
                } else if (returnValue.getClass() == String.class) {
                    modelAndView.setViewName(returnValue.toString());
                } else if (returnValue.getClass() == ModelAndView.class) {
                    return (ModelAndView) returnValue;
                } else {
                    modelAndView.addAttr(returnValue);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return modelAndView;
    }


}

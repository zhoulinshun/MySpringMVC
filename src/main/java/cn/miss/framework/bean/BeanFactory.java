package cn.miss.framework.bean;

import cn.miss.framework.Logger;
import cn.miss.framework.annonation.AConstructor;
import cn.miss.framework.annonation.Inject;
import cn.miss.framework.aop.ProxyFactory;
import cn.miss.framework.entity.AspectBean;
import cn.miss.framework.entity.BeanEntity;
import cn.miss.framework.exception.BeanDependenceException;
import cn.miss.framework.exception.BeanNotFoundException;
import cn.miss.framework.exception.ConstructorParamException;
import cn.miss.framework.select.BeanBuilder;
import cn.miss.framework.select.MVCBeanBuilder;
import cn.miss.framework.util.MethodUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author MissNull
 * @Description: 负责实例的提供
 * @Date: Created in 2017/10/9.
 */
public class BeanFactory {
    private final BeanBuilder selector;
    private Map<Class, String> classMap = new HashMap<>();
    private Map<String, BeanEntity> beanEntityMap = new HashMap<>();
    private Map<Class, Object> singletonInstance = new ConcurrentHashMap<>();
    private Map<Class, Object> tempInstance = new ConcurrentHashMap<>();
    private Map<Class, Boolean> createFlag = new ConcurrentHashMap<>();//当前类是否在构建过程中;
    //key 被需要的类，List<List<String>> 所需类的所有字段集合
    private Map<Class, Map<Object, List<String>>> requireDependent = new ConcurrentHashMap<>();
    private Lock lock = new ReentrantLock();
    private Lock lock2 = new ReentrantLock();
    private BeanCore beanCore;
    private ProxyFactory proxyFactory;
    private boolean aop;

    public BeanFactory(String packName) {
        this(packName, false);
    }

    public BeanFactory(String packName, boolean aop) {
        this(new BeanCore(packName), new MVCBeanBuilder(), aop);
    }

    public BeanFactory(BeanCore beanCore, BeanBuilder selector, boolean aop) {
        this.selector = selector;
        this.beanCore = beanCore;
        this.aop = aop;
        init(selector);
    }

    private void init(BeanBuilder selector) {
        Logger.log("BeanFactory.init");
        beanEntityMap = selector.select(beanCore);
        beanEntityMap.forEach((s, beanEntity) -> classMap.put(beanEntity.getBeanClass(), s));
        proxyFactory = new ProxyFactory(this, aop);
    }


    public Collection<BeanEntity> getBeanEntities() {
        return beanEntityMap.values();
    }

    public BeanEntity getBeanEntity(String name) {
        return beanEntityMap.get(name);
    }

    public BeanEntity getBeanEntity(Class clazz) {
        return getBeanEntity(classMap.get(clazz));
    }

    public Map<String, AspectBean> getAspectBean(BeanBuilder beanBuilder) {
        return beanBuilder.select(this);
    }

    public Object getBean(Class clazz) {
        return getBean(clazz, true);
    }

    private Object getBean(Class clazz, boolean flag) {
        return getBean(classMap.get(clazz), flag);
    }

    public Object getBean(String name) {
        //外部调用时为true，此时清空临时map
        return getBean(name, true);
    }

    private Object getBean(String name, boolean flag) {
        BeanEntity beanEntity = getBeanEntity(name);
        if (beanEntity == null) {
            throw new BeanNotFoundException();
        }
        try {
            if (beanEntity.isSingleton()) {
                lock2.lock();
                Object o = singletonInstance.get(beanEntity.getBeanClass());
                if (o == null) {
                    //创建实例
                    createFlag.put(beanEntity.getBeanClass(), true);
                    o = createBean(beanEntity.getBeanClass(), singletonInstance);
                }
                lock2.unlock();
                return o;
            } else {
                lock.lock();
                if (flag) tempInstance.clear();
                createFlag.put(beanEntity.getBeanClass(), true);
                Object instance = createBean(beanEntity.getBeanClass(), tempInstance);
                lock.unlock();
                return instance;
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new BeanDependenceException(e.getMessage());
        }
    }

    private Object createBean(Class clazz, Map<Class, Object> instanceMap) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object o = createInstance(clazz);
        instanceMap.put(clazz, o);
        createFlag.put(clazz, false);
        Map<Object, List<String>> classListMap = requireDependent.get(o.getClass());
        if (classListMap != null) {
            classListMap.forEach((object, s) -> {
                for (String str : s) {
                    try {
                        Field declaredField = object.getClass().getDeclaredField(str);
                        declaredField.setAccessible(true);
                        declaredField.set(object, o);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            classListMap.remove(o.getClass());
        }
        //依赖注入
        dependenceInvoke(o);
        return o;
    }

    private void dependenceInvoke(Object o) throws IllegalAccessException {
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            //如果已经设置或默认值  或者在构造方法中被注入过，此处就不在注入
            if (declaredField.get(o) != null) continue;
            Inject inject;
            if ((inject = declaredField.getAnnotation(Inject.class)) != null) {
                Object object = inject(inject, declaredField.getType(), declaredField.getName(), false, o);
                declaredField.set(o, object);
            }
        }

    }

    /**
     * 构造器参数注入或字段注入
     *
     * @param inject        注解对象
     * @param fieldType     参数类型
     * @param fieldName     参数名称
     * @param isConstructor 是否是构造器注入
     * @param parent        如果是构造器注入，则该类型是构造类的类型
     *                      如果是字段注入，该类型为字段所在类的对象
     *                      此处是为了避免构造器相互循环注入
     * @return
     */
    private Object inject(Inject inject, Class fieldType, String fieldName, boolean isConstructor, Object parent) {
        //如果所需对象正在构建过程中；
        Boolean flag = createFlag.get(fieldType);
        if (flag != null && flag) {
            if (isConstructor)
                throw new BeanDependenceException(((Class) parent).getName() + ":" + fieldType.getName() + "出现循环构造器依赖");
            Map<Object, List<String>> classListMap = requireDependent.get(fieldType);
            if (classListMap == null) {
                HashMap<Object, List<String>> classListHashMap = new HashMap<>();
                classListHashMap.put(parent, Collections.singletonList(fieldName));
                requireDependent.put(fieldType, classListHashMap);
            } else {
                List<String> strings = classListMap.get(parent);
                if (strings == null) {
                    strings = Collections.singletonList(fieldName);
                    classListMap.put(parent, strings);
                } else {
                    strings.add(fieldName);
                }
            }
            return null;
        }
        String name = inject.name();
        Class type = inject.type();
        if (name.equals("")) {
            if (type == Object.class) {
                BeanEntity beanEntity = beanEntityMap.get(fieldName);
                if (beanEntity == null) {
                    beanEntity = getBeanEntity(type);
                    if (beanEntity == null)
                        throw new ConstructorParamException(type.getName() + ":  " + fieldType.getName() + ":" + fieldName + " 所指定的bean不存在");
                }
                Class beanClass = beanEntity.getBeanClass();
                Constructor paramConstructor = getConstructor(beanClass.getConstructors());
                if (paramConstructor == null) {
                    throw new ConstructorParamException();
                }
                if (isConstructor) {
                    for (Parameter parameter : paramConstructor.getParameters()) {
                        if (parameter.getType() == parent.getClass()) throw new BeanDependenceException();
                    }
                }
                return getBean(beanEntity.getBeanClass(), false);
            } else {
                return getBean(type, false);
            }
        } else {
            return getBean(name, false);
        }

    }

    //创建对象实例
    private Object createInstance(Class clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Object returnValue;
        Constructor[] constructors = clazz.getConstructors();
        Constructor constructor = getConstructor(constructors);
        if (constructor == null) {
            throw new BeanDependenceException("缺少合适的构造方法");
        }
        if (constructor.getParameterCount() == 0) {
            returnValue = constructor.newInstance();
        } else {
            Map<String, Inject> methodParamAnnotation = MethodUtil.getMethodParamAnnotation(Inject.class, constructor);
            Map<String, Class> methodParam = MethodUtil.getMethodParam(constructor);
            String[] parameterNames = MethodUtil.getParameterNames(constructor);
            Object[] params = new Object[parameterNames.length];
            for (int i = 0; i < params.length; i++) {
                Inject inject = methodParamAnnotation.get(parameterNames[i]);
                params[i] = inject(inject, methodParam.get(parameterNames[i]), parameterNames[i], true, clazz);
            }
            returnValue = constructor.newInstance(params);
        }
        if (beanEntityMap.get(classMap.get(clazz)).getAop() != null) {
            return proxyFactory.proxy(returnValue);
        }
        return returnValue;
    }

    //获取合适的构造器
    private Constructor getConstructor(Constructor[] constructors) {
        Constructor c = null;
        for (Constructor constructor : constructors) {
            if (constructor.getAnnotation(AConstructor.class) != null) {
                Map<String, Inject> methodParamAnnotation = MethodUtil.getMethodParamAnnotation(Inject.class, constructor);
                if (methodParamAnnotation.size() != constructor.getParameterCount()) {
                    throw new ConstructorParamException();
                }
                return constructor;
            }
            if (constructor.getParameterCount() == 0) {
                c = constructor;
            }
        }
        return c;
    }
}

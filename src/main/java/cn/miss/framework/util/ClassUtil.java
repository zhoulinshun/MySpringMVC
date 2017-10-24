package cn.miss.framework.util;

import cn.miss.mvc.other.ClassEntity;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @Author MissNull
 * @Description: 解析类注入
 * @Date: Created in 2017/9/5.
 */
public class ClassUtil {

    //获取指定包下的所有类全名
    public static List<String> getClasses(String packName) {
        String newPackName = packName.replace(".", "\\");
        List<String> list = new ArrayList<>();

        try {
            //jar路径
            String runtimePath = JarUtil.getJarLocation();
            if (runtimePath.contains("/")) {
                runtimePath = runtimePath.replace("/", "\\");
                runtimePath = runtimePath.substring(1);//开头有个\
            }
            if (runtimePath.endsWith(".jar")) {
                JarFile jarFile = new JarFile(runtimePath);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String name = jarEntry.getName();//  cn/miss/entity/ZHAnswer.class
                    if (name.endsWith(".class")) {
                        if (name.startsWith(newPackName.substring(1))) {
                            list.add(name.replace("\\", ".").replace("/", "."));
                        }
                    }
                }
            } else {
                //class路径
                File file = new File(runtimePath + "" + newPackName);
                List<String> collection = new ArrayList<>();
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    if (files != null) {
                        for (File f : files) {
                            collection.addAll(getFileNames(f));
                        }
                        for (String s : collection) {
                            list.add(s.substring(runtimePath.length()).replace("\\", "."));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getFileNames(File file) {
        List<String> strings = new ArrayList<>();
        if (file != null) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        strings.addAll(getFileNames(f));
                    }
                }
            } else {
                if (file.getName().endsWith(".class")) {
                    strings.add(file.getAbsolutePath());
                }
            }
        }
        return strings;
    }

    //获取指定包类的所有class对象
    public static List<Class> getClassInstances(String pack) {
        List<String> packages = getClasses(pack);
        List<Class> list = new ArrayList<>();
        for (String s : packages) {
            try {
                list.add(Class.forName(s.substring(0, s.lastIndexOf("."))));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static List<ClassEntity> getClassEntityByPackageName(String pack) {
        List<ClassEntity> list = new ArrayList<>();
        List<Class> classInstances = getClassInstances(pack);
        classInstances.forEach(clazz -> list.add(new ClassEntity(clazz)));
        return list;
    }

    public static <D extends Annotation> List<ClassEntity> getClassEntityByPackageAndAnnotation(String pack, Class<D> annotation) {
        Objects.requireNonNull(pack);
        List<ClassEntity> entities = getClassEntityByPackageName(pack);
        return entities.stream().
                filter(c -> annotation == null || c.containAnnotation(annotation)).
                collect(Collectors.toList());
    }


    //获取指定包内注解类和特定子类
    public static <T, D extends Annotation>
    Map<D, Class<T>> getInstancesByAnnotationAndSuper(String pack, Class<D> annotation, Class<T> superClass) {
        Objects.requireNonNull(pack);
        Map<D, Class<T>> map = new HashMap<>();
        List<ClassEntity> entities = getClassEntityByPackageName(pack);
        entities.stream().filter(c -> (superClass == null || c.isSuperClass(superClass))
                && (annotation == null || c.containAnnotation(annotation)))
                .forEach(c -> map.put(c.getAnnotation(annotation), c.getClazz()));
        return map;
    }

    /**
     * @param pack
     * @param annotation
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T, D extends Annotation>
    Map<D, Class<T>> getInstancesByAnnotation(String pack, Class<D> annotation) {
        return getInstancesByAnnotationAndSuper(pack, annotation, null);
    }

    public static <D extends Annotation>
    List<Class> getInstancesByAnnotation2(String pack, Class<D> annotation) {
        List<Class> classInstances = getClassInstances(pack);
        return classInstances.stream().filter(aClass -> aClass.getAnnotation(annotation) != null).collect(Collectors.toList());
    }

    public static <D> List<Class> getInstancesBySuper(String pack, Class<D> superClass) {
        Objects.requireNonNull(pack);
        List<ClassEntity> entities = getClassEntityByPackageName(pack);
        return entities.stream().filter(c -> c.isSuperClass(superClass))
                .map(ClassEntity::getClazz).collect(Collectors.toList());
    }

    public static List<Class> getSuperClass(Class clazz) {
        List<Class> superClasses = new ArrayList<>();
        Class superClass = clazz.getSuperclass();
        while (true) {
            if (superClass.equals(Object.class)) break;
            superClasses.add(superClass);
            superClass = superClass.getSuperclass();
        }
        return superClasses;
    }

}

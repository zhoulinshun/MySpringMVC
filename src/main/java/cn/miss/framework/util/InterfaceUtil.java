package cn.miss.framework.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/9/21.
 */
public class InterfaceUtil {

    public static List<Class> getInterface(Class clazz) {
        List<Class> list = new ArrayList<>();
        Class[] interfaces = clazz.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            list.addAll(getInterface(interfaces[i]));
        }
        return list;
    }
}

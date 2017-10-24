package cn.miss.framework.util;

import java.io.File;
import java.net.URL;

/**
 * @Author MissNull
 * @Description: jar相关
 * @Date: Created in 2017/9/6.
 */
public class JarUtil {

    public static String getJarParentLocation() {
        String v;
        if ((v = getJarLocation()) != null) {
            File parentFile = new File(v).getParentFile();
            return parentFile.getAbsolutePath();
        }
        return null;
    }

    //获取当前jar运行目录
    public static String getJarLocation() {
        URL url = JarUtil.class.getProtectionDomain().getCodeSource().getLocation();
        return url.getPath();
    }

    public static String getPackageLocation(String pack) {
        return JarUtil.class.getResource(pack).getPath();
    }

}


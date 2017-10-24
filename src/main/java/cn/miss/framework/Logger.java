package cn.miss.framework;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/10.
 */
public class Logger {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static void log(String msg) {
        String format = dateFormat.format(new Date());
        System.out.println(format + ":" + msg);
    }
}

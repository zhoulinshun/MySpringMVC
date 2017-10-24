package cn.miss.framework.core;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/23.
 */
public class CoreConfig {
    private boolean isAop;
    private String packageName;
    private String prefix;
    private String suffix;


    public CoreConfig(boolean isAop, String packageName, String prefix, String suffix) {
        this.isAop = isAop;
        this.packageName = packageName;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public boolean isAop() {
        return isAop;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}

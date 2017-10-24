package cn.miss.framework.mvc.request;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/9.
 */
public enum HttpRequestMethod {
    POST("post"), GET("get"), HEAD("head");

    private final String value;

    private HttpRequestMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

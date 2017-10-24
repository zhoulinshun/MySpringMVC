package cn.miss.framework.exception;

/**
 * @Author MissNull
 * @Description: 实体类构造方法不合法，
 * 当缺少无参构造方法或者有参构造方法参数上缺少@Param注解时会抛出此错误
 * @Date: Created in 2017/10/12.
 */
public class ConstructorParamException extends RuntimeException {
    public ConstructorParamException() {
        this("实体类构造方法不合法，当缺少无参构造方法或者有参构造方法参数上缺少@Param注解或注解的name和type不一致时会抛出此错误");
    }

    public ConstructorParamException(String message) {
        super(message);
    }

    public ConstructorParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstructorParamException(Throwable cause) {
        super(cause);
    }

    public ConstructorParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

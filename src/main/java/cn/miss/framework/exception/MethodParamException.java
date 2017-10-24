package cn.miss.framework.exception;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/9/21.
 */
public class MethodParamException extends RuntimeException{
    public MethodParamException() {
    }

    public MethodParamException(String message) {
        super(message);
    }

    public MethodParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodParamException(Throwable cause) {
        super(cause);
    }

    public MethodParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

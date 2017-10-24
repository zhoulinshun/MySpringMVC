package cn.miss.framework.exception;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/9/21.
 */
public class ClassNoSupportException extends RuntimeException {
    public ClassNoSupportException() {
    }

    public ClassNoSupportException(String message) {
        super(message);
    }

    public ClassNoSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassNoSupportException(Throwable cause) {
        super(cause);
    }

    public ClassNoSupportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

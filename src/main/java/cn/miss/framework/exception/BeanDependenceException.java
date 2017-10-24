package cn.miss.framework.exception;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/13.
 */
public class BeanDependenceException extends RuntimeException {
    public BeanDependenceException() {
        this("当出现构造方法循环依赖时会抛出此异常");
    }

    public BeanDependenceException(String message) {
        super(message);
    }

    public BeanDependenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanDependenceException(Throwable cause) {
        super(cause);
    }

    public BeanDependenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

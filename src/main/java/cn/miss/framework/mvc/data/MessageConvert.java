package cn.miss.framework.mvc.data;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/11.
 */
public interface MessageConvert<T> {
    T convert(String msg);
}

package cn.miss.framework.mvc.model;

import org.springframework.core.Conventions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/8.
 */
public class Model {
    private Map<String, Object> attr;

    public Model() {
        this(new HashMap<>());
    }

    public Model(Map<String, Object> attr) {
        this.attr = attr;
    }

    public void addAttr(String key, Object value) {
        attr.put(key, value);
    }

    public void addAttr(Object value) {
        if (value instanceof Collection && ((Collection) value).isEmpty())
            return;
        attr.put(Conventions.getVariableName(value), value);
    }

    public void addAllAttrs(Map<String, Object> attrs) {
        attr.putAll(attrs);
    }

    public Object getAttr(String key) {
        return attr.get(key);
    }

    public Map<String, Object> getModel() {
        return attr;
    }
}

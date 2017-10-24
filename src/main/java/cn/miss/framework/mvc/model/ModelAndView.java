package cn.miss.framework.mvc.model;

import cn.miss.framework.mvc.view.ServletView;
import cn.miss.framework.mvc.view.ViewType;

import java.util.Map;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/8.
 */
public class ModelAndView {
    private Model model;
    private ServletView view;

    public ModelAndView() {
        this(null, new Model());
    }

    public ModelAndView(String viewName, Model model) {
        view = new ServletView(viewName);
        this.model = model;
    }

    public void addAttr(String key, Object value) {
        model.addAttr(key, value);
    }

    public void addAttr(Object value) {
        model.addAttr(value);
    }

    public void addAllAttrs(Map<String, Object> attrs) {
        model.addAllAttrs(attrs);
    }

    public Object getAttr(String key) {
        return model.getAttr(key);
    }

    public String getViewName() {
        return view.getViewName();
    }

    public void setViewName(String viewName) {
        view.setViewName(viewName);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ServletView getView() {
        return view;
    }

    public void setView(ServletView view) {
        this.view = view;
    }

    public void setViewType(ViewType type) {
        view.setType(type);
    }

    public ViewType getViewType() {
        return view.getType();
    }
}

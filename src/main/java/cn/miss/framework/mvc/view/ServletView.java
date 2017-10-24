package cn.miss.framework.mvc.view;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/15.
 */
public class ServletView {
    private String viewName;
    //默认转发
    private ViewType type;

    public ServletView(String viewName) {
        this(viewName,ViewType.forward);
    }

    public ServletView(String viewName, ViewType type) {
        this.viewName = viewName;
        this.type = type;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public ViewType getType() {
        return type;
    }

    public void setType(ViewType type) {
        this.type = type;
    }
}

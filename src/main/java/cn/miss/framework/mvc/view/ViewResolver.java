package cn.miss.framework.mvc.view;

import cn.miss.framework.mvc.model.ModelAndView;
import com.sun.istack.internal.NotNull;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/10/8.
 */
public class ViewResolver {
    private String prefix;
    private String suffix;

    public ViewResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public ModelAndView getView(ModelAndView modelAndView, HttpServletRequest req) {
        String view = modelAndView.getViewName();
        if (view == null) {
            view = req.getRequestURI();
        } else {
            if (view.startsWith("redirect:")) {
                modelAndView.setViewType(ViewType.redirect);
            }
        }
        view = uriUtil(uriUtil(prefix, view), suffix);
        modelAndView.setViewName(view);
        return modelAndView;
    }

    private String uriUtil(String prefix, String suffix) {
        if (prefix.endsWith("/")) prefix = prefix.substring(0, prefix.length() - 2);
        if (suffix.startsWith("/")) suffix = suffix.substring(1);
        if (suffix.equals(""))
            return prefix;
        return prefix + "/" + suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}

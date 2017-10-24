package cn.miss.framework.mvc;

import cn.miss.framework.bean.BeanFactory;
import cn.miss.framework.mvc.handler.Handler;
import cn.miss.framework.mvc.handler.HandlerAdapter;
import cn.miss.framework.mvc.handler.HandlerMapper;
import cn.miss.framework.mvc.model.ModelAndView;
import cn.miss.framework.mvc.request.HttpRequestMethod;
import cn.miss.framework.mvc.view.ViewResolver;
import cn.miss.framework.mvc.view.ViewType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @Author MissNull
 * @Description:
 * @Date: Created in 2017/9/18.
 */
@WebServlet(name = "myServlet",
        urlPatterns = "*.action",
        initParams = {
                @WebInitParam(name = "basePackName", value = "cn.miss.controller"),
                @WebInitParam(name = "prefix", value = ""),
                @WebInitParam(name = "suffix", value = ""),
        })
public class DispatcherServlet extends HttpServlet {
    private static final ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.http.LocalStrings");
    private String basePackName;
    private BeanFactory beanFactory;
    private HandlerMapper handlerMapper;
    private HandlerAdapter handlerAdapter;
    private ViewResolver viewResolver;

    /**
     * 1.收集指定包下的所有类，构造BeanCore
     * 2.收集指定注解的或继承类的bean,并生成对应的Handler
     * 3.构造HandlerAdapter
     * 4.构造HandlerMapper
     * 5
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        basePackName = getInitParameter("basePackName");
        String prefix = getInitParameter("prefix");
        String suffix = getInitParameter("suffix");
        beanFactory = new BeanFactory(basePackName);
        handlerMapper = new HandlerMapper(beanFactory);
        handlerAdapter = new HandlerAdapter(beanFactory);
        viewResolver = new ViewResolver(prefix, suffix);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            dispatch(req, resp, HttpRequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            dispatch(req, resp, HttpRequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void dispatch(HttpServletRequest req, HttpServletResponse resp, HttpRequestMethod method) throws Exception {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        Handler handler = handlerMapper.getHandler(req);
        if (handler == null) {
            resp.sendError(404, "请求的资源不存在");
            return;
        }
        if (!handler.isSupport(method)) {
            String protocol = req.getProtocol();
            String msg = lStrings.getString("http.method_" + method.getValue() + "_not_supported");
            if (protocol.endsWith("1.1")) {
                resp.sendError(405, msg);
            } else {
                resp.sendError(400, msg);
            }
            return;

        }
        ModelAndView modelAndView = handlerAdapter.handler(handler, req, resp);
        if (modelAndView == null) {
            return;
        }
        String viewName = modelAndView.getViewName();
        if (viewName == null) {
            //返回json
            Object json = modelAndView.getAttr("return_json_miss");
            if (json != null) {
                try {
                    resp.getWriter().write(json.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                return;
            }
        }
        modelAndView = viewResolver.getView(modelAndView, req);
        if (modelAndView.getViewType() == ViewType.forward) {
            Map<String, Object> model = modelAndView.getModel().getModel();
            model.forEach(req::setAttribute);
            req.getRequestDispatcher(modelAndView.getViewName()).forward(req, resp);
        } else {
            resp.sendRedirect(modelAndView.getViewName());
        }
    }


}

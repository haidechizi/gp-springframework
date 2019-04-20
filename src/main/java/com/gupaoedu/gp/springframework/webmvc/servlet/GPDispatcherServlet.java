package com.gupaoedu.gp.springframework.webmvc.servlet;

import com.gupaoedu.gp.springframework.annotation.GPController;
import com.gupaoedu.gp.springframework.annotation.GPRequestMapping;
import com.gupaoedu.gp.springframework.context.GPApplicationContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class GPDispatcherServlet extends HttpServlet {

    private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private static final String TEMPLATE_ROOT = "templateRoot";

    private GPApplicationContext context;

    private List<GPHandlerMapping> handlerMappings = new ArrayList<>();

    private Map<GPHandlerMapping, GPHandlerAdapter> handlerAdapterMap = new ConcurrentHashMap<>();

    private List<GPViewResolver> viewResolvers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            doCreateExceptionHandler(req,resp,e);
        }

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        GPHandlerMapping handlerMapping = getHandler(req);
        if (handlerMapping == null) {

            //resp.getWriter().write("404 Not Found");
            doCreateNptFoundHandler(req,resp);
            return;

        }

        GPHandlerAdapter handlerAdapter = getHandlerAdapter(handlerMapping);
        if (handlerAdapter == null) {
            //resp.getWriter().write("404 Not Found");
            doCreateNptFoundHandler(req,resp);
            return;
        }

        GPModelAndView modelAndView = handlerAdapter.handler(req, resp, handlerMapping);

        processDispatchResult(req, resp, modelAndView);

    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, GPModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            return;
        }

        if (this.viewResolvers.isEmpty()) {
            return;
        }

        for (GPViewResolver viewResolver : this.viewResolvers) {
            GPview view = viewResolver.resolveViewname(modelAndView.getViewName());
            view.render(modelAndView.getModel(), req, resp);
            return;
        }
    }

    private GPHandlerAdapter getHandlerAdapter(GPHandlerMapping handlerMapping) {
        if (this.handlerAdapterMap.isEmpty()) {
            return null;
        }
        GPHandlerAdapter handlerAdapter = this.handlerAdapterMap.get(handlerMapping);
        if (handlerAdapter.support(handlerMapping)) {
            return handlerAdapter;
        }
        return null;
    }

    private GPHandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }
        String pathInfo = req.getPathInfo().replaceAll("/+", "/");
        for (GPHandlerMapping handlerMapping : this.handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(pathInfo);
            if (matcher.matches()) {
                return handlerMapping;
            }
        }
        return null;

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("init server");
        String configLocation = config.getInitParameter(CONTEXT_CONFIG_LOCATION);
        context = new GPApplicationContext(configLocation);
        try {
            this.initStrategies(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initStrategies(GPApplicationContext context) throws Exception {
        initMultipartResolver(context);
        initLocaleResolver(context);
        initThemeResolver(context);
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initHandlerExceptionResolvers(context);
        initRequestToViewNameTranslator(context);
        initViewResolvers(context);
        initFlashMapManager(context);
    }

    private void initFlashMapManager(GPApplicationContext context) {
    }

    private void initViewResolvers(GPApplicationContext context) {
        String templateRoot = context.getConfig().getProperty(TEMPLATE_ROOT);
        // 不能使用这个
        //this.getClass().getResource(templateRoot).getFile();
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        File[] listFiles = templateRootDir.listFiles();

        for (int i = 0; i < listFiles.length; i++) {
            this.viewResolvers.add(new GPViewResolver(templateRoot, context.getConfig().getProperty("suffix")));
        }

    }

    private void initRequestToViewNameTranslator(GPApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(GPApplicationContext context) {
    }

    private void initHandlerAdapters(GPApplicationContext context) {
        for (GPHandlerMapping handlerMapping : this.handlerMappings) {
            handlerAdapterMap.put(handlerMapping, new GPHandlerAdapter());
        }

    }

    private void initHandlerMappings(GPApplicationContext context) throws Exception {
        for (String beanName : context.beanDefinitionNames()) {
            Object instance = context.getBean(beanName);
            Class<?> clazz = instance.getClass();
            if (!clazz.isAnnotationPresent(GPController.class)) {
                continue;
            }
            String baseUrl = "";
            if (clazz.isAnnotationPresent(GPRequestMapping.class)) {
                GPRequestMapping gpRequestMapping = clazz.getAnnotation(GPRequestMapping.class);
                baseUrl = gpRequestMapping.value();
            }


            // 只获取public方法
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(GPRequestMapping.class)) {
                    continue;
                }
                GPRequestMapping gpRequestMapping = method.getAnnotation(GPRequestMapping.class);
                String methodUrl = "/" + baseUrl + "/" + gpRequestMapping.value();


                GPHandlerMapping handlerMapping = doCreateHandlerMapping(instance, method, methodUrl);

                this.handlerMappings.add(handlerMapping);

            }
        }
    }

    private GPHandlerMapping doCreateHandlerMapping(Object controller, Method method, String url) {
        url = url.replaceAll("/+", "/").replaceAll("\\*", ".*");
        Pattern pattern = Pattern.compile(url);
        GPHandlerMapping handlerMapping = new GPHandlerMapping(controller, method, pattern);
        log.info("Mapped url :" + url);
        return handlerMapping;
    }

    private void initThemeResolver(GPApplicationContext context) {

    }

    private void initLocaleResolver(GPApplicationContext context) {
    }

    private void initMultipartResolver(GPApplicationContext context) {
    }

    private void doCreateExceptionHandler(HttpServletRequest request,HttpServletResponse response,Throwable e) {
        GPModelAndView modelAndView = new GPModelAndView();
        modelAndView.setViewName("500");
        Map<String,Object> model = new HashMap<>();
        String detail = e.getMessage();
        if(detail == null || "".equals(detail.trim())) {
            detail = e.getCause().getMessage();
        }
        if(detail == null) {
            detail = "服务异常";
        }
        model.put("detail",detail);
        model.put("stackTrace",e.getStackTrace().toString());
        modelAndView.setModel(model);
        try {
            processDispatchResult(request, response, modelAndView);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void doCreateNptFoundHandler(HttpServletRequest request,HttpServletResponse response) {
        GPModelAndView modelAndView = new GPModelAndView();
        modelAndView.setViewName("404");

        try {
            processDispatchResult(request, response, modelAndView);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}

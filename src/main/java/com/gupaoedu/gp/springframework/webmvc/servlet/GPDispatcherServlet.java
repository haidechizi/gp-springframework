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
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class GPDispatcherServlet extends HttpServlet {

    private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private GPApplicationContext context;

    private List<GPHandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
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
    }

    private void initRequestToViewNameTranslator(GPApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(GPApplicationContext context) {
    }

    private void initHandlerAdapters(GPApplicationContext context) {
        for (GPHandlerMapping handlerMapping : this.handlerMappings) {

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


                GPHandlerMapping handlerMapping = doCreatehandlerMapping(instance, method, methodUrl);

                this.handlerMappings.add(handlerMapping);

            }
        }
    }

    private GPHandlerMapping doCreatehandlerMapping(Object controller, Method method, String url) {
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
}

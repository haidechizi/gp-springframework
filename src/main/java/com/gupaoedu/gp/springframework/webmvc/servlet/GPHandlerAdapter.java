package com.gupaoedu.gp.springframework.webmvc.servlet;

import com.gupaoedu.gp.springframework.annotation.GPRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class GPHandlerAdapter {

    public boolean support(Object obj) {
        return obj instanceof GPHandlerMapping;
    }

    public GPModelAndView handler(HttpServletRequest req, HttpServletResponse resp, GPHandlerMapping handlerMapping) throws InvocationTargetException, IllegalAccessException {
        Map<String, Integer> paramIndexmapping = new HashMap<>();
        Annotation[][] annotations = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof GPRequestParam) {
                    GPRequestParam requestParam = (GPRequestParam) annotation;
                    String value = requestParam.value();
                    paramIndexmapping.put(value, i);
                    continue;
                }
            }
        }

        Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> clazz = parameterTypes[i];
            if (clazz == HttpServletRequest.class || clazz == HttpServletResponse.class) {
                paramIndexmapping.put(clazz.getName(), i);
            }
        }

        Map<String, String[]> parameterMap = req.getParameterMap();
        Object[] paramValues = new Object[parameterTypes.length];
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

            String[] values = entry.getValue();
            String value = arraysToString(values);
            int index = paramIndexmapping.get(entry.getKey());
            Object resValue = cover(parameterTypes[index], value);

            paramValues[index] = value;
        }
        if (paramIndexmapping.containsKey(HttpServletRequest.class.getName())) {
            int index = paramIndexmapping.get(HttpServletRequest.class.getName());
            paramValues[index] = req;
        }

        if (paramIndexmapping.containsKey(HttpServletResponse.class.getName())) {
            int index = paramIndexmapping.get(HttpServletResponse.class.getName());
            paramValues[index] = req;
        }

        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);

        if (result == null || result instanceof Void) {
            return null;
        }
        if (result instanceof GPModelAndView) {
            return (GPModelAndView) result;
        }

        return null;

    }

    private Object cover(Class<?> clazz, String value) {
        if (Integer.class == clazz || int.class == clazz) {
            return Integer.valueOf(value);
        } else if (Double.class == clazz || double.class == clazz) {
            return Double.valueOf(value);
        }
        return value;
    }

    private String arraysToString(String[] values) {
        if (values != null && values.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                sb.append(values[i]);
                if (i != values.length - 1) {
                    sb.append(",");
                }
            }
            return sb.toString();

        }
        return "";

    }
}

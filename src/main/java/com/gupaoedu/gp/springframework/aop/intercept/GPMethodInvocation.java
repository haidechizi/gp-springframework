package com.gupaoedu.gp.springframework.aop.intercept;

import com.gupaoedu.gp.springframework.aop.aspect.GPJoinPoint;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class GPMethodInvocation implements GPJoinPoint {

    private Object proxy;
    private Method method;
    private Object target;
    private Object[] arguments;
    private List<Object> interceptorAndDynamicMethodMatchers;
    private Class<?> targetClass;
    private Map<String, Object> userAttributes;
    private int currentInterceptorIndex = -1;

    public GPMethodInvocation(Object proxy, Object target, Method method
            , Object[] arguments, Class<?> targetClass, List<Object> interceptorAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.method = method;
        this.target = target;
        this.arguments = arguments;
        this.targetClass = targetClass;
        this.interceptorAndDynamicMethodMatchers = interceptorAndDynamicMethodMatchers;
    }

    public Object proceed() throws Throwable {
        if (this.currentInterceptorIndex == interceptorAndDynamicMethodMatchers.size() - 1) {
            return this.method.invoke(this.target, this.arguments);
        }
        Object interceptorOrInterceptorAdvise = this.interceptorAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);

        if (interceptorOrInterceptorAdvise instanceof GPMethodInterceptor) {
            GPMethodInterceptor mi = (GPMethodInterceptor) interceptorOrInterceptorAdvise;
            return mi.invoke(this);
        } else {
            return proceed();
        }
    }

    @Override
    public Object getThis() {
        return null;
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Method getMethod() {
        return null;
    }

    @Override
    public void setUserAttribute(String key, Object value) {

    }

    @Override
    public Object getUserAttribute(String key) {
        return null;
    }
}

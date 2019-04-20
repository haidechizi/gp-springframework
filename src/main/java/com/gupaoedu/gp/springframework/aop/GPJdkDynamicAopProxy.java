package com.gupaoedu.gp.springframework.aop;

import com.gupaoedu.gp.springframework.aop.intercept.GPMethodInterceptor;
import com.gupaoedu.gp.springframework.aop.intercept.GPMethodInvocation;
import com.gupaoedu.gp.springframework.aop.support.GPAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class GPJdkDynamicAopProxy implements GPAopProxy, InvocationHandler {

    private GPAdvisedSupport advisedSupport;

    public GPJdkDynamicAopProxy(GPAdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advisedSupport.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        Object proxyInstance = Proxy.newProxyInstance(classLoader, this.advisedSupport.getTargetClass().getInterfaces(), this);
        return proxyInstance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicInterceptionAdvice = this.advisedSupport.getInterceptorsAndDynamicInterceptionAdvice(method, this.advisedSupport.getTargetClass());
        GPMethodInvocation methodInvocation = new GPMethodInvocation(proxy, this.advisedSupport.getTarget(),
                method, args, this.advisedSupport.getTargetClass(), interceptorsAndDynamicInterceptionAdvice);
        return methodInvocation.proceed();
    }
}

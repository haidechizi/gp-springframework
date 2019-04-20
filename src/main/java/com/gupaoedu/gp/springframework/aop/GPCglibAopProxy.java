package com.gupaoedu.gp.springframework.aop;

import com.gupaoedu.gp.springframework.aop.intercept.GPMethodInvocation;
import com.gupaoedu.gp.springframework.aop.support.GPAdvisedSupport;
import com.gupaoedu.gp.springframework.test.Test;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

public class GPCglibAopProxy implements GPAopProxy, MethodInterceptor {

    private GPAdvisedSupport advisedSupport;

    public GPCglibAopProxy(GPAdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return this.getProxy(this.advisedSupport.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.advisedSupport.getTargetClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        List<Object> interceptorsAndDynamicInterceptionAdvice = this.advisedSupport.getInterceptorsAndDynamicInterceptionAdvice(method, this.advisedSupport.getTargetClass());
        GPMethodInvocation methodInvocation = new GPMethodInvocation(methodProxy, this.advisedSupport.getTarget(),
                method, objects, this.advisedSupport.getTargetClass(), interceptorsAndDynamicInterceptionAdvice);
        return methodInvocation.proceed();
    }
}

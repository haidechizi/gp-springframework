package com.gupaoedu.gp.springframework.aop.intercept;

public interface GPMethodInterceptor {

    Object invoke(GPMethodInvocation methodInvocation) throws Throwable;
}

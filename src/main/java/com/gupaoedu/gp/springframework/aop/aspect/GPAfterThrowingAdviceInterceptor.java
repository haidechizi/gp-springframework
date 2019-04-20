package com.gupaoedu.gp.springframework.aop.aspect;

import com.gupaoedu.gp.springframework.aop.intercept.GPMethodInterceptor;
import com.gupaoedu.gp.springframework.aop.intercept.GPMethodInvocation;

import java.lang.reflect.Method;

public class GPAfterThrowingAdviceInterceptor extends GPAbstractAspectAdvice implements GPAdvice, GPMethodInterceptor {


    public GPAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(GPMethodInvocation methodInvocation) throws Throwable {

        try {
            return methodInvocation.proceed();
        } catch (Throwable e) {
            super.invokeAdviseMethod(methodInvocation, null, e);
            throw e;
        }
    }
}

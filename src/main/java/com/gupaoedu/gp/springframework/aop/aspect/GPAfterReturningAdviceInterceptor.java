package com.gupaoedu.gp.springframework.aop.aspect;

import com.gupaoedu.gp.springframework.aop.intercept.GPMethodInterceptor;
import com.gupaoedu.gp.springframework.aop.intercept.GPMethodInvocation;

import java.lang.reflect.Method;

public class GPAfterReturningAdviceInterceptor extends GPAbstractAspectAdvice implements GPAdvice,GPMethodInterceptor {
    private GPJoinPoint joinPoint;

    public GPAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(GPMethodInvocation methodInvocation) throws Throwable {
        this.joinPoint = methodInvocation;
        Object returnValue = methodInvocation.proceed();
        after(methodInvocation.getMethod(),returnValue,methodInvocation.getArguments(),methodInvocation.getThis());
        return returnValue;
    }

    private void after(Method method, Object returnValue, Object[] arguments, Object aThis) throws Throwable {
        super.invokeAdviseMethod(this.joinPoint,returnValue,null);
    }
}

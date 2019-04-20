package com.gupaoedu.gp.springframework.aop.aspect;

import com.gupaoedu.gp.springframework.aop.intercept.GPMethodInterceptor;
import com.gupaoedu.gp.springframework.aop.intercept.GPMethodInvocation;

import java.lang.reflect.Method;

public class GPMethodBeforeAdviceInterceptor extends GPAbstractAspectAdvice implements GPAdvice, GPMethodInterceptor {

    private GPJoinPoint joinPoint;

    public GPMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(GPMethodInvocation methodInvocation) throws Throwable {
        this.joinPoint = methodInvocation;
        before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
        return methodInvocation.proceed();
    }

    private void before(Method method, Object[] arguments, Object aThis) throws Throwable {
        super.invokeAdviseMethod(this.joinPoint,null,null);
    }
}

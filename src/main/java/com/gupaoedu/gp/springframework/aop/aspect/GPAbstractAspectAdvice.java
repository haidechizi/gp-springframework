package com.gupaoedu.gp.springframework.aop.aspect;

import java.lang.reflect.Method;

public class GPAbstractAspectAdvice implements GPAdvice {

    private Method aspectMethod;
    private Object aspectTarget;

    public GPAbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    public Object invokeAdviseMethod(GPJoinPoint joinPoint, Object returnValue, Throwable t) throws Throwable {
        Class<?>[] parameterTypes = this.aspectMethod.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length <= 0) {
            return this.aspectMethod.invoke(this.aspectTarget);
        }

        Object[] paramValues = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == GPJoinPoint.class) {
                paramValues[i] = joinPoint;
            } else if (parameterTypes[i] == Throwable.class) {
                paramValues[i] = t;
            } else if (parameterTypes[i] == Object.class) {
                paramValues[i] = returnValue;
            }
        }
        return this.aspectMethod.invoke(this.aspectTarget, paramValues);
    }
}

package com.gupaoedu.gp.springframework.aop;

public interface GPAopProxy {

    Object getProxy();

    Object getProxy(ClassLoader classLoader);
}

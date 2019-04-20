package com.gupaoedu.gp.demo.aspect;

public class LogAspect {

    public void before() {
        System.out.println("before method");
    }

    public void after() {
        System.out.println(" after method");
    }

    public void afterThrowing() {
        System.out.println("after throwing");
    }
}

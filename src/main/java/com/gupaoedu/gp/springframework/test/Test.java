package com.gupaoedu.gp.springframework.test;

import com.gupaoedu.gp.demo.service.impl.TestServiceImpl;
import com.gupaoedu.gp.springframework.context.GPApplicationContext;

public class Test {

    public static void main(String[] args) {
        GPApplicationContext applicationContext = new GPApplicationContext("classpath:application.properties");

        try {
            //Object instance = applicationContext.getBean(TestController.class);
            Object instance = applicationContext.getBean(TestServiceImpl.class);
            System.out.println(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

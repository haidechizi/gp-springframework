package com.gupaoedu.gp.demo.web;

import com.gupaoedu.gp.demo.service.ITestService;
import com.gupaoedu.gp.springframework.annotation.GPAutowired;
import com.gupaoedu.gp.springframework.annotation.GPController;
import com.gupaoedu.gp.springframework.annotation.GPRequestMapping;
import com.gupaoedu.gp.springframework.annotation.GPRequestParam;
import com.gupaoedu.gp.springframework.webmvc.servlet.GPModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@GPController
public class TestController {

    @GPAutowired
    private ITestService iTestService;

    @GPRequestMapping(value = "index")
    public GPModelAndView test(@GPRequestParam("name") String name) {
        System.out.println("index " + name);
        Map<String,Object> map = new HashMap<>();
        name = iTestService.test(name);
        map.put("name",name);
        map.put("teacher",name);
        map.put("data",new Date());
        map.put("token", UUID.randomUUID().toString());
        GPModelAndView modelAndView = new GPModelAndView("first",map);
        return modelAndView;
    }

    @GPRequestMapping(value = "index1")
    public GPModelAndView test1(@GPRequestParam("name") String name) {

        Map<String,Object> map = new HashMap<>();
        name = iTestService.test1(name);
        map.put("name",name);
        map.put("teacher",name);
        map.put("data",new Date());
        map.put("token", UUID.randomUUID().toString());
        GPModelAndView modelAndView = new GPModelAndView("first",map);
        return modelAndView;
    }
}

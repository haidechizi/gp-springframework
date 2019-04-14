package com.gupaoedu.gp.demo.web;

import com.gupaoedu.gp.demo.service.ITestService;
import com.gupaoedu.gp.springframework.annotation.GPAutowired;
import com.gupaoedu.gp.springframework.annotation.GPController;

@GPController
public class TestController {

    @GPAutowired
    private ITestService iTestService;

    public String test(String naem) {
        return iTestService.test(naem);
    }
}

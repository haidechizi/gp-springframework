package com.gupaoedu.gp.demo.service.impl;

import com.gupaoedu.gp.demo.dao.TestDao;
import com.gupaoedu.gp.demo.service.ITestService;
import com.gupaoedu.gp.springframework.annotation.GPAutowired;
import com.gupaoedu.gp.springframework.annotation.GPService;

@GPService
public class TestServiceImpl implements ITestService {

    @GPAutowired
    private TestDao testDao;

    @Override
    public String test(String name) {
        System.out.println("接收到请求：" + name);
        name = testDao.test(name);
        return "Hello " + name;
    }
}

package com.gupaoedu.gp.demo.dao;

import com.gupaoedu.gp.springframework.annotation.GPComponent;

@GPComponent
public class TestDao {

    public String test(String name) {
        return name;
    }
}

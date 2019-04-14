package com.gupaoedu.gp.springframework.core;

import java.util.List;

public interface GPBeanFactory {


    /**
     * 根据beanName获取bean
     *
     * @param beanName
     * @return
     */
    Object getBean(String beanName) throws Exception;


    Object getBean(Class<?> clazz) throws Exception;

    List<String> beanDefinitionNames();

    int beanDefinitionNamesCount();
}

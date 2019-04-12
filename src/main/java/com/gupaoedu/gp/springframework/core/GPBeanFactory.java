package com.gupaoedu.gp.springframework.core;

public interface GPBeanFactory {


    /**
     * 根据beanName获取bean
     *
     * @param beanName
     * @return
     */
    Object getBean(String beanName);
}

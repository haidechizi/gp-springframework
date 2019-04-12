package com.gupaoedu.gp.springframework.beans.support;

import com.gupaoedu.gp.springframework.beans.config.GPBeanDefinition;
import com.gupaoedu.gp.springframework.context.support.GPAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GPDefaultListableBeanFactory extends GPAbstractApplicationContext {

    protected final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, GPBeanDefinition>();
}

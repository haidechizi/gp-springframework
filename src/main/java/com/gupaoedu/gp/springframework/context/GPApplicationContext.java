package com.gupaoedu.gp.springframework.context;

import com.gupaoedu.gp.springframework.annotation.GPAutowired;
import com.gupaoedu.gp.springframework.annotation.GPComponent;
import com.gupaoedu.gp.springframework.annotation.GPController;
import com.gupaoedu.gp.springframework.annotation.GPService;
import com.gupaoedu.gp.springframework.beans.GPBeanWrapper;
import com.gupaoedu.gp.springframework.beans.config.GPBeanDefinition;
import com.gupaoedu.gp.springframework.beans.support.GPBeanDefinitionReader;
import com.gupaoedu.gp.springframework.beans.support.GPDefaultListableBeanFactory;
import com.gupaoedu.gp.springframework.core.GPBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {

    private String[] configLocatons;

    private GPBeanDefinitionReader reader;

    private Map<String, Object> singletonBeanMap = new ConcurrentHashMap<>(64);


    private Map<String, GPBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>(64);

    public GPApplicationContext(String... configLocatons) {
        this.configLocatons = configLocatons;
        try {
            this.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void refresh() throws Exception {
        reader = new GPBeanDefinitionReader(this.configLocatons);

        List<GPBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        doRegistryBeanDefinitions(beanDefinitions);

        doAutowired();
    }

    private void doRegistryBeanDefinitions(List<GPBeanDefinition> beanDefinitions) {
        for (GPBeanDefinition beanDefinition : beanDefinitions) {
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }


    }

    private void doAutowired() {
        for (Map.Entry<String, GPBeanDefinition> entry : this.beanDefinitionMap.entrySet()) {
            GPBeanDefinition beanDefinition = entry.getValue();
            // 是单例的，并且不是延时加载的类
            if (!beanDefinition.isLazyInit() && beanDefinition.isSingleton()) {
                // 加载对象
                try {
                    getBean(entry.getKey());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public Object getBean(String beanName) throws Exception {
        if (this.singletonBeanMap.containsKey(beanName)) {
            return this.singletonBeanMap.get(beanName);
        }

        //1、初始化
        Object instance = instantiateBean(beanName, this.beanDefinitionMap.get(beanName));
        GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);
        if (this.factoryBeanInstanceCache.containsKey(beanName)) {
            throw new Exception(beanName + " is exists");
        }
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);

        //2、注入
        populateBean(beanName, new GPBeanDefinition(), beanWrapper);


        return instance;
    }

    private void populateBean(String beanName, GPBeanDefinition gpBeanDefinition, GPBeanWrapper gpBeanWrapper) throws Exception {
        Class<?> clazz = gpBeanWrapper.getWrapperClass();
        // 只注入有注解的类
        if (clazz.isAnnotationPresent(GPController.class) || clazz.isAnnotationPresent(GPService.class)
                || clazz.isAnnotationPresent(GPComponent.class)) {

            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isAnnotationPresent(GPAutowired.class)) {
                    continue;
                }
                GPAutowired autowired = field.getAnnotation(GPAutowired.class);
                String fieldName = autowired.value();
                if ("".equals(fieldName)) {
                    fieldName = field.getName();
                }
                Object instance = this.singletonBeanMap.get(fieldName);
                if (instance == null) {
                    continue;
                }
                field.set(gpBeanWrapper.getWrapperInstance(), instance);
            }
        }

    }

    private Object instantiateBean(String beanName, GPBeanDefinition gpBeanDefinition) throws Exception {
        String clazzName = gpBeanDefinition.getBeanClassName();
        Class<?> clazz = Class.forName(clazzName);
        Object instance = clazz.newInstance();
        if (gpBeanDefinition.isSingleton()) {
            this.singletonBeanMap.put(beanName, instance);
        }

        return instance;
    }

    @Override
    public Object getBean(Class<?> clazz) throws Exception {
        return this.getBean(clazz.getName());
    }

    @Override
    public List<String> beanDefinitionNames() {
        return this.beanDefinitionMap.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public int beanDefinitionNamesCount() {
        return this.beanDefinitionMap.keySet().size();
    }
}

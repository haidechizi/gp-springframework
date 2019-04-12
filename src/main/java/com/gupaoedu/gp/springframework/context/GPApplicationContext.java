package com.gupaoedu.gp.springframework.context;

import com.gupaoedu.gp.springframework.beans.config.GPBeanDefinition;
import com.gupaoedu.gp.springframework.beans.support.GPBeanDefinitionReader;
import com.gupaoedu.gp.springframework.beans.support.GPDefaultListableBeanFactory;
import com.gupaoedu.gp.springframework.context.support.GPAbstractApplicationContext;
import com.gupaoedu.gp.springframework.core.GPBeanFactory;

import java.util.List;

public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {

    private String [] configLocatons;

    private GPBeanDefinitionReader reader;

    public GPApplicationContext(String ... configLocatons) {
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
        for(GPBeanDefinition beanDefinition : beanDefinitions) {
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
        }


    }

    private void doAutowired() {
    }

    @Override
    public Object getBean(String beanName) {
        return null;
    }
}

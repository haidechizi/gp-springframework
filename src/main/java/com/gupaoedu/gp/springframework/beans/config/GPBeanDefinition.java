package com.gupaoedu.gp.springframework.beans.config;

public class GPBeanDefinition {

    private String beanClassName;
    private boolean lazyInif = false;

    private String factoryBeanName;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInif() {
        return lazyInif;
    }

    public void setLazyInif(boolean lazyInif) {
        this.lazyInif = lazyInif;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}

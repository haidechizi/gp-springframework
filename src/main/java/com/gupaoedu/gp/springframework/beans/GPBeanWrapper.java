package com.gupaoedu.gp.springframework.beans;

public class GPBeanWrapper {

    private Object wrapperInstance;

    public GPBeanWrapper(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public Object getWrapperInstance() {
        return this.wrapperInstance;
    }

    public Class<?> getWrapperClass() {
        return this.wrapperInstance.getClass();
    }
}

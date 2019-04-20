package com.gupaoedu.gp.springframework.beans;

public class GPBeanWrapper {

    private Object wrapperInstance;

    private Object wrapperTargetInstance;

    public GPBeanWrapper() {

    }

    public GPBeanWrapper(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public void setWrapperInstance(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public Class<?> getWrapperClass() {
        return this.wrapperInstance.getClass();
    }

    public Object getWrapperTargetInstance() {
        return wrapperTargetInstance;
    }

    public void setWrapperTargetInstance(Object wrapperTargetInstance) {
        this.wrapperTargetInstance = wrapperTargetInstance;
    }
}

package com.gupaoedu.gp.springframework.aop.support;

import com.gupaoedu.gp.springframework.aop.aspect.GPAfterReturningAdviceInterceptor;
import com.gupaoedu.gp.springframework.aop.aspect.GPAfterThrowingAdviceInterceptor;
import com.gupaoedu.gp.springframework.aop.aspect.GPMethodBeforeAdviceInterceptor;
import com.gupaoedu.gp.springframework.aop.config.GPAopConfig;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPAdvisedSupport {

    private GPAopConfig aopConfig;

    private transient Map<Method, List<Object>> methodCache = new ConcurrentHashMap<>();

    public GPAdvisedSupport(GPAopConfig aopConfig) {
        this.aopConfig = aopConfig;
    }

    private Object target;

    private Class<?> targetClass;

    private Pattern pointCutClassPattern;

    public GPAopConfig getAopConfig() {
        return aopConfig;
    }

    public void setAopConfig(GPAopConfig aopConfig) {
        this.aopConfig = aopConfig;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    private void parse() {
        //1.将切面转成正则表达式
        String pointCut = aopConfig.getPointCut().replaceAll("\\.", "\\\\.")
                .replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)")
                .replaceAll("\\\\.\\*", ".*");
        String pointCutForClassReg = pointCut.substring(0, pointCut.lastIndexOf("\\("));
        pointCutClassPattern = Pattern.compile("class" + pointCutForClassReg.substring(7));

        Pattern pattern = Pattern.compile(pointCut);
        try {
            Class<?> clazz = Class.forName(this.getAopConfig().getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<>();
            for (Method method : clazz.getMethods()) {
                aspectMethods.put(method.getName(), method);
            }

            for (Method method : this.getTargetClass().getMethods()) {
                String methodString = method.toString();
                if (methodString.contains("throws")) {
                    methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();

                }
                Matcher matcher = pattern.matcher(methodString);
                if (matcher.matches()) {
                    List<Object> list = new LinkedList<>();
                    if (this.aopConfig.getAspectBefore() != null && !"".equals(this.aopConfig.getAspectBefore().trim())) {
                        list.add(new GPMethodBeforeAdviceInterceptor(aspectMethods.get(this.getAopConfig().getAspectBefore()),
                                clazz.newInstance()));
                    }

                    if (this.aopConfig.getAspectAfter() != null && !"".equals(this.aopConfig.getAspectAfter().trim())) {
                        list.add(new GPAfterReturningAdviceInterceptor(aspectMethods.get(this.aopConfig.getAspectAfter()),
                                clazz.newInstance()));
                    }
                    if (this.aopConfig.getAspectAfterThrowing() != null && !"".equals(this.aopConfig.getAspectAfterThrowing().trim())) {
                        list.add(new GPAfterThrowingAdviceInterceptor(aspectMethods.get(this.aopConfig.getAspectAfterThrowing()),
                                clazz.newInstance()));
                    }
                    methodCache.put(method,list);
                }

            }
        } catch (Exception e) {

        }

    }

    public Pattern getPointCutClassPattern() {
        return pointCutClassPattern;
    }

    public void setPointCutClassPattern(Pattern pointCutClassPattern) {
        this.pointCutClassPattern = pointCutClassPattern;
    }

    public boolean pointMatch() {
        String classStr = this.targetClass.toString();
        return this.pointCutClassPattern.matcher(classStr).matches();
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception {
        List<Object> objects = this.methodCache.get(method);
        if (objects == null) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            objects = methodCache.get(m);
            this.methodCache.put(m,objects);
        }
        return objects;
    }
}

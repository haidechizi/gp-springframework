package com.gupaoedu.gp.springframework.beans.support;

import com.gupaoedu.gp.springframework.beans.config.GPBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GPBeanDefinitionReader {

    private Properties properties = new Properties();

    private final String SCAN_PACKAGE = "scanPackage";

    List<String> classNames = new ArrayList<>();


    public GPBeanDefinitionReader(String... configLocations) {
        String configLocation = configLocations[0].replace("classpath:", "");
        InputStream is = this.getClass().getResourceAsStream("/" + configLocation);
        //InputStream is = this.getClass().getClassLoader().getResourceAsStream(configLocation);

        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(properties.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String sacnPackage) {

        String filesDir = sacnPackage.replace(".", "/");
        //URL url = this.getClass().getClassLoader().getResource("/" + filesDir);
        URL url = this.getClass().getResource("/" + filesDir);
        File file = new File(url.getFile());
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                doScanner(sacnPackage + "." + f.getName());
            } else {
                String fileName = f.getName();
                if (fileName.endsWith(".class")) {
                    // 不要.class
                    classNames.add(sacnPackage + "." + fileName.replace(".class", ""));
                }
            }
        }
    }


    public List<GPBeanDefinition> loadBeanDefinitions() {
        List<GPBeanDefinition> beanDefinitions = new ArrayList<>();
        try {
            for (String className : classNames) {

                Class<?> clazz = Class.forName(className);
                if (clazz.isInterface()) {
                    continue;

                }
                beanDefinitions.add(doCreateBeanDefinition(toLowerCaseFirst(clazz.getSimpleName()), clazz));
                beanDefinitions.add(doCreateBeanDefinition(className, clazz));

                for (Class<?> i : clazz.getInterfaces()) {
                    beanDefinitions.add(doCreateBeanDefinition(i.getName(), clazz));
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return beanDefinitions;
    }


    private GPBeanDefinition doCreateBeanDefinition(String beanName, Class<?> clazz) {
        GPBeanDefinition beanDefinition = new GPBeanDefinition();
        beanDefinition.setFactoryBeanName(beanName);
        beanDefinition.setBeanClassName(clazz.getName());
        return beanDefinition;
    }

    private String toLowerCaseFirst(String name) {
        char[] chars = name.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public Properties getConfig() {
        return this.properties;
    }

}



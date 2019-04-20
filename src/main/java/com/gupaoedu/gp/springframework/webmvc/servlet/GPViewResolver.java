package com.gupaoedu.gp.springframework.webmvc.servlet;

import org.omg.PortableInterceptor.SUCCESSFUL;

import java.io.File;

public class GPViewResolver {

    private String suffix;

    File templateRootDir;
    public GPViewResolver() {

    }

    public GPViewResolver(String templateRoot, String suffix) {
        this.suffix = suffix;
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        templateRootDir = new File(templateRootPath);
    }

    public GPview resolveViewname(String viewName) {
        if (viewName == null || "".equals(viewName.trim())) {
            return null;
        }
        viewName = viewName.endsWith(suffix) ? viewName : viewName + suffix;
        File file = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
        GPview view = new GPview(file);
        return view;
    }
}

package com.gupaoedu.gp.springframework.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPview {
    File viewFile;

    public GPview() {

    }

    public GPview(File file) {
        this.viewFile = file;
    }

    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        StringBuilder sb = new StringBuilder();

        RandomAccessFile raf = new RandomAccessFile(this.viewFile, "r");

        String line = null;
        while ((line = raf.readLine()) != null) {
            line = new String(line.getBytes("ISO-8859-1"), "UTF-8");
            Pattern pattern = Pattern.compile("￥\\{[^{]+\\}", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String paramName = matcher.group();
                paramName = paramName.replaceAll("￥\\{|\\}", "");
                Object value = model.get(paramName);
                if (value == null) {
                    continue;
                }
                line = matcher.replaceFirst(value.toString());
                matcher = pattern.matcher(line);
            }
            sb.append(line);

        }
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(sb.toString());

    }
}

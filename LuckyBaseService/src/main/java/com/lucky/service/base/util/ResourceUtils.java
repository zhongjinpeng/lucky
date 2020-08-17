package com.lucky.service.base.util;

import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * @author <a href="mailto:jpzhong1994@gmail.com">zhongjinpeng</a>
 * @version 1.0
 * @date 2020/7/16 09:04
 *
 */
public class ResourceUtils {

    public static InputStream getResourceByPath(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        return classPathResource.getInputStream();
    }

    public static String inputStreamConvertString(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}

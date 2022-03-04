package com.ndirituedwin.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.ndirituedwin.Utils.AppConstants.USERAVATARS;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
private final long MAX_AGE_SECS=3600;

    @Value("${app.cors.allowedOrigins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);

    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
////        WebMvcConfigurer.super.addResourceHandlers(registry);
//        exposeDirectory(USERAVATARS,registry);
//    }
//
//    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
//        Path uploadDir= Paths.get(dirName);
//        log.info("logging uploadDir from WebMvcconfig {}",uploadDir);
//        String uploadPath=uploadDir.toFile().getAbsolutePath();
//        log.info("logging uploadPath from WebMvcconfig {}",uploadPath);
//
//        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
//        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+ uploadPath + "/");
//    }

}

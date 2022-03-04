package com.ndirituedwin.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.ndirituedwin.Utils.AppConstants.USERAVATARS;


@Component
@ConfigurationProperties(prefix="file")
public class FileStorageProperties {

    private String uploadDir;
//    private String uploadDir=USERAVATARS;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }


}
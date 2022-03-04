package com.ndirituedwin.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class FileUploadUtil {


    public static void saveFile(String uploadDir, String filename, MultipartFile multipartFile) throws IOException {

        Path uploadPath= Paths.get(uploadDir);
        log.info("logging uploadPath from FileUpoadUtil {}",uploadPath);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try {
            InputStream inputStream=multipartFile.getInputStream();
            log.info("loggging inputStream {}",inputStream);
            Path filePath=uploadPath.resolve(filename);
            log.info("logging filePath {}",filePath);
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException ex){
            throw new IOException("Could not save Image file "+filename,ex);
        }
    }
}

package com.ndirituedwin.Service;

import com.ndirituedwin.Config.FileStorageProperties;
import com.ndirituedwin.Dto.responses.FileResponse;
import com.ndirituedwin.Entity.User;
import com.ndirituedwin.Exception.FileStorageException;
import com.ndirituedwin.Exception.MyFileNotFoundException;
import com.ndirituedwin.Repository.UserRepository;
import com.ndirituedwin.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileStorageService {
    private final Path fileStorageLocation;
    private final UserRepository userRepository;
    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties, UserRepository userRepository) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.userRepository = userRepository;

        try {
            Files.createDirectories(this.fileStorageLocation);

        }catch(Exception ex) {
            throw new FileStorageException("Could not create the directory to upload");
        }
    }
    //	function to store the file
    public FileResponse storeFile(UserPrincipal currentUser, MultipartFile file) {

        String filenametostore = null;
        String filenamee= StringUtils.cleanPath(file.getOriginalFilename());
        log.info("logging filenamee {}",filenamee);

        String fileExtension = filenamee.substring(filenamee.lastIndexOf("."));
        log.info("logging file extension {}",fileExtension);

        User user=userRepository.findById(currentUser.getId()).orElseThrow(() -> new UsernameNotFoundException("user with the given id could not be found"+currentUser.getId()));
         log.info("logging user {}",user);

        String[] fille=filenamee.split("[.]");
        log.info("logging file {}",fille);

        List<String> firstindex = Collections.singletonList(Arrays.stream(fille).collect(Collectors.toList()).get(0));
        List<String> secondIndex = Collections.singletonList(Arrays.stream(fille).collect(Collectors.toList()).get(1));

        log.info("logging the first index in filename {} ",firstindex.get(0));
        log.info("logging the second index in filename {} ",secondIndex.get(0));

        filenametostore=firstindex.get(0)+"_"+currentUser.getId()+currentUser.getUsername()+"."+secondIndex.get(0);
        log.info("logging filetostore {}",filenametostore);

        log.info("logging extension {}",fileExtension);
        log.info("logging original filename {}",filenamee);
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        log.info("fileName {}",fileName);
        try {

            Path targetLocation = this.fileStorageLocation.resolve(filenametostore);
               log.info("logging target location {} ",targetLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
         log.info("file stored {}",filenametostore);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/auth/uploadimage/")
                    .path(filenametostore)
                    .toUriString();
            log.info("logging the dowload uri {}",fileDownloadUri);
            user.setAvatar(fileDownloadUri);
            userRepository.save(user);
            FileResponse fileResponse = new FileResponse(filenametostore, fileDownloadUri, file.getContentType(), file.getSize());

            return fileResponse;
        }catch(IOException ex) {
            throw new FileStorageException("Could not store file"+filenametostore + ". Please try again!",ex);
        }
    }
    //	function to load the file
    public Resource loadFileAsResource(String fileName) {
        try {
            log.info("loading filename {}",fileName);
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            log.info("loading filepath {}",filePath);
            Resource resource = new UrlResource(filePath.toUri());
            log.info("loading resource {}",resource);
            if(resource.exists()) {
                return resource;
            }else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        }catch(MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName);
        }
    }
}

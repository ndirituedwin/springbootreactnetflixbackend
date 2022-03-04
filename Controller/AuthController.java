package com.ndirituedwin.Controller;


import com.ndirituedwin.Dto.LoginRequest;
import com.ndirituedwin.Dto.SignUpRequest;
import com.ndirituedwin.Dto.responses.FileResponse;
import com.ndirituedwin.Dto.responses.JwtAuthenticationResponse;
import com.ndirituedwin.Entity.User;
import com.ndirituedwin.Exception.DocumentStorageException;
import com.ndirituedwin.Repository.UserRepository;
import com.ndirituedwin.Service.AuthService;
import com.ndirituedwin.Service.FileStorageService;
import com.ndirituedwin.security.CurrentUser;
import com.ndirituedwin.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.ndirituedwin.Utils.AppConstants.USERAVATARS;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final AuthService  authService;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;


    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateuser(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.signin(loginRequest));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpRequest signUpRequest) throws DocumentStorageException, IOException {
       return new ResponseEntity<>(authService.register(signUpRequest),HttpStatus.OK);
    }
//    @PostMapping("/uploadimage")
//    public ResponseEntity<?>upload(@CurrentUser UserPrincipal currentUser, @RequestParam("avatar")MultipartFile file) throws IOException {
//        return new ResponseEntity<>(authService.uploadAvatar(currentUser,file),HttpStatus.OK);
//    }


    @PostMapping("/uploadfile")
    public ResponseEntity<?> uploadavatar(@CurrentUser UserPrincipal currentUser,@RequestParam("avatar")MultipartFile file) throws IOException {
        return new ResponseEntity<>(authService.saveavatar(currentUser,file),HttpStatus.CREATED);
    }
    @GetMapping("/getuseravatar")
    public ResponseEntity<?> getuseravatarr(@CurrentUser UserPrincipal principal){
        return ResponseEntity.ok(authService.getcurrentuseravatar(principal));
    }
    @PostMapping("/uploadimage")
    public ResponseEntity<FileResponse>upload(@CurrentUser UserPrincipal currentUser, @RequestParam("avatar")MultipartFile file) throws IOException {

//
//        String fileName = fileStorageService.storeFile(currentUser,file);
//        log.info("logging file stored {}",fileName);
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/api/auth/uploadimage/")
//                .path(fileName)
//                .toUriString();
//        log.info("logging the dowload uri {}",fileDownloadUri);

//        FileResponse fileResponse = new FileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        return new ResponseEntity<FileResponse>(fileStorageService.storeFile(currentUser,file),HttpStatus.OK);
    }
    @GetMapping("/uploadimage/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){

        Resource resource = fileStorageService.loadFileAsResource(fileName);
        log.info("loading resurce {}",resource);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
              log.info("loading content Type {}",contentType);
        }catch(IOException ex) {
            System.out.println("Could not determine fileType");
        }

        if(contentType==null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }





//    @GetMapping(path = "/getcurrentuseravatar",produces = {IMAGE_PNG_VALUE,IMAGE_JPEG_VALUE,IMAGE_GIF_VALUE})
//    public Object getuseravatar(@CurrentUser UserPrincipal currentUser) throws IOException {
////        User user=userRepository.findById(currentUser.getId()).orElseThrow(() -> new UsernameNotFoundException("not found "+currentUser.getId()));
////        return Files.readAllBytes(Paths.get(System.getProperty("user.home")+"/Downloads/images/servertwo.png"));
////        return Files.readAllBytes(Paths.get(System.getProperty(USERAVATARS)+"/"+user.getId()+"/angel_3nyakiago.jpg"));
//          return authService.getuseravatar(currentUser);
//
//    }



}

package com.ndirituedwin.Service;

import com.ndirituedwin.Config.AppConfig;
import com.ndirituedwin.Dto.LoginRequest;
import com.ndirituedwin.Dto.SignUpRequest;
import com.ndirituedwin.Dto.responses.ApiResponse;
import com.ndirituedwin.Dto.responses.JwtAuthenticationResponse;
import com.ndirituedwin.Dto.responses.UserAvatarResponse;
import com.ndirituedwin.Dto.responses.imageresponse;
import com.ndirituedwin.Entity.Enum.RoleName;
import com.ndirituedwin.Entity.NotificationEmail;
import com.ndirituedwin.Entity.Role;
import com.ndirituedwin.Entity.User;
import com.ndirituedwin.Utils.FileUploadUtil;
import com.ndirituedwin.Entity.VerificationToken;
import com.ndirituedwin.Exception.AppException;
import com.ndirituedwin.Exception.DocumentStorageException;
import com.ndirituedwin.Repository.RoleRepository;
import com.ndirituedwin.Repository.UserRepository;
import com.ndirituedwin.Repository.VerificationTokenRepository;
import com.ndirituedwin.Service.mail.MailService;
import com.ndirituedwin.security.JwtTokenProvider;
import com.ndirituedwin.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ndirituedwin.Utils.AppConstants.USERAVATARS;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {
//    private final Path fileStorageLocation= Path.of(USERAVATARS);
    private static final String  Bearer="Bearer";
    private static final String subject="Please activate your account";
    private static final String  body="Thank you for signing up to the polling application,click on the link below to activate your account";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final AppConfig appConfig;
    private final VerificationTokenRepository verificationTokenRepository;



    public JwtAuthenticationResponse signin(LoginRequest loginRequest) {

        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameorEmail(),loginRequest.getPassword()));
        log.info("logging authentication {}",authentication.getName());
        log.info("logging authentication {}",authentication.getPrincipal());
        log.info("logging authentication {}",authentication.getCredentials());
        log.info("logging authentication {}",authentication.getDetails());

        SecurityContextHolder.getContext().setAuthentication(authentication);
      String token=jwtTokenProvider.generateToken(authentication);
      log.info("loggig token {}",token);
      log.info("logging authentiction after it is set {}",SecurityContextHolder.getContext().getAuthentication());
//      return new JwtAuthenticationResponse(token,Bearer,"");
        return JwtAuthenticationResponse.builder()
                .accessToken(token)
                .usernameorEmail(loginRequest.getUsernameorEmail())
                .tokenType(Bearer)
                .expiresAt(Instant.now().plusMillis(jwtTokenProvider.getJwtExpirationInMillis()))
                .build();
    }

    public Object register(SignUpRequest signUpRequest){
         //checking whether they exists
        if (userRepository.existsByUsername(signUpRequest.getUsername())){
            return new ResponseEntity(new ApiResponse(false,"username "+signUpRequest.getUsername()+" is already taken"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())){
            return new ResponseEntity(new ApiResponse(false,"email "+signUpRequest.getEmail()+" is already taken"),HttpStatus.BAD_REQUEST);
        }
        User user=new User();
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEnabled(false);


        Role userrole=roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("user role not yet set"));
        user.setRoles(Collections.singleton(userrole));
        User result=userRepository.save(user);



        String vtoken=generateverificationToken(user);
        log.info("logging vtoken {}",vtoken);
        mailService.sendEmail(new NotificationEmail(subject,user.getEmail(),body+"\n "+appConfig.getUrl()+"api/auth/accountverification/"+vtoken));
        log.info("Logging the token before sending email to the user {} ",vtoken);
        URI location= ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true,"user registered successfully"));
    }
    private String generateverificationToken(User user) {
        String token= UUID.randomUUID().toString();
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;

    }

    public Object saveavatar(UserPrincipal currentUser, MultipartFile multipartFile) throws IOException {

        log.info("multipart file {}",multipartFile);
        String filenametostore=null;
        String filename=multipartFile.getOriginalFilename();
        log.info("getting the fiLe name {}",filename);
        String fileextension=filename.substring(filename.lastIndexOf("."));
        log.info("logging extension {}",fileextension);
        User user=userRepository.findById(currentUser.getId()).orElseThrow(() -> new UsernameNotFoundException("user with the given id could not be found"+currentUser.getId()));
   if (filename.contains(".")){
       String[] file=filename.split("[.]");
       List<String> firstindex = Collections.singletonList(Arrays.stream(file).collect(Collectors.toList()).get(0));
       List<String> secondIndex = Collections.singletonList(Arrays.stream(file).collect(Collectors.toList()).get(1));
       log.info("logging the first index in filename {} ",firstindex.get(0));
       log.info("logging the second index in filename {} ",secondIndex.get(0));
      filenametostore= firstindex.get(0)+"_"+currentUser.getId()+currentUser.getUsername()+UUID.randomUUID()+"."+secondIndex.get(0);
       log.info("logging filetostore {}",filenametostore);
       user.setAvatar(filenametostore);
       userRepository.save(user);
       String uploadDir=USERAVATARS.concat("/").concat(String.valueOf(currentUser.getId()));
       log.info("logging the upload directory {}",uploadDir);

       FileUploadUtil.saveFile(uploadDir, filenametostore, multipartFile);



   }
       return filenametostore;
    }
    public Object uploadAvatar(UserPrincipal currentUser, MultipartFile multipartFile) throws IOException {
        String filenametostore = null;
        /*handle file upload*/
        String filename=StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String fileExtension = filename.substring(filename.lastIndexOf("."));
        User user=userRepository.findById(currentUser.getId()).orElseThrow(() -> new UsernameNotFoundException("user with the given id could not be found"+currentUser.getId()));

        if (filename.contains(".")){
            String[] file=filename.split("[.]");
//           Arrays.stream(file).collect(Collectors.toList());
            List<String> firstindex = Collections.singletonList(Arrays.stream(file).collect(Collectors.toList()).get(0));
            List<String> secondIndex = Collections.singletonList(Arrays.stream(file).collect(Collectors.toList()).get(1));
            log.info("logging the first index in filename {} ",firstindex.get(0));
            log.info("logging the second index in filename {} ",secondIndex.get(0));
//          log.info("splitting filename {}",  Arrays.stream(file).collect(Collectors.toList()).get(0));
               filenametostore=firstindex.get(0)+"_"+currentUser.getId()+currentUser.getUsername()+"."+secondIndex.get(0);
              log.info("logging filetostore {}",filenametostore);
        }

        log.info("logging extension {}",fileExtension);
        log.info("logging original filename {}",filename);
        user.setAvatar(filenametostore);
        userRepository.save(user);
        String uploadDir=USERAVATARS.concat("/").concat(String.valueOf(currentUser.getId()));
        log.info("logging the upload directory {}",uploadDir);

        FileUploadUtil.saveFile(uploadDir, filenametostore, multipartFile);

        URI location= ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(currentUser.getUsername()).toUri();
        log.info("logging the location {} ",location);

        return ResponseEntity.created(location).body(new ApiResponse(true,"file uploaded successfully "));

        /*end*/
    }
    public Object getuseravatar(UserPrincipal currentUser) throws FileNotFoundException {
        final Path fileStorageLocation= Path.of(USERAVATARS.concat("/").concat(String.valueOf(currentUser.getId())));
          User user=userRepository.findById(currentUser.getId()).orElseThrow(() -> new UsernameNotFoundException("user witth id "+currentUser.getId()+" could not be found"));
        try {
            Path filePath=fileStorageLocation.resolve(user.getAvatar()).normalize();
            Resource resource=new UrlResource(filePath.toUri());
            if (resource.exists()){
                log.info("logging the resorce {} ",resource);
                return resource;
//                return ResponseEntity.ok(new imageresponse(resource));
//                return resource.getFilename();
//              return  ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/getcurrentuseravatar/"+user.getAvatar()).toUriString();

              //                URI location= ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/getuseravatar/{username}")
//                        .buildAndExpand(currentUser.getUsername()).toUri();
//                log.info("logging the location {} ",location);
//
//                return ResponseEntity.created(location).body(new imageresponse(filePath.toString()));

            }else{
                throw new FileNotFoundException("file not found "+user.getAvatar());
            }

        }catch (MalformedURLException | FileNotFoundException e){
            throw new FileNotFoundException("file with name "+user.getAvatar() +" could not be found");
        }
    }

    public UserAvatarResponse getcurrentuseravatar(UserPrincipal principal) {
        User user=userRepository.findById(principal.getId()).orElseThrow(() -> new UsernameNotFoundException("user with given id not found "+principal.getId()));
        try {
            log.info("user avatar {}",user.getAvatar());
            return UserAvatarResponse.builder()
                    .username(principal.getUsername())
                    .avatar(user.getAvatar())
                    .message("image fetched successfully")
                    .build();
        }catch (Exception e){
            log.info("an exceptio has occurred while retrieving the image {}",e);
            return UserAvatarResponse.builder()
                    .message("an exception has occurred while trying to fetch user avatar "+e.getMessage())
                    .build();
        }
    }


//    public byte[] getuseravatarr(UserPrincipal currentUser) {
//    }
}

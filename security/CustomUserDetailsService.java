package com.ndirituedwin.security;

import com.ndirituedwin.Entity.User;
import com.ndirituedwin.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

     @Autowired
    UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameoremail) throws UsernameNotFoundException {
        // allow them to login using emaik or username;
        User user=userRepository.findByUsernameOrEmail(usernameoremail,usernameoremail)
                .orElseThrow(() -> new UsernameNotFoundException("user with email or username "+usernameoremail+"  could not be found"));
          log.info("logging the user found {}",user);
          UserPrincipal principal=UserPrincipal.create(user);
          log.info("logging the principal after a creating userprincipal {}",principal);
        return principal;

    }
    //the following function will be used by the jwtauthenticationfilter
    @Transactional
    public UserDetails loadUserById(Long id){
        User user=userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("user with id"+id+" could not be found"));
        return UserPrincipal.create(user);
    }
}

package com.ndirituedwin.Service;

import com.ndirituedwin.Dto.responses.UserSummary;
import com.ndirituedwin.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {


    public UserSummary getCurrentuser(UserPrincipal currentUser) {

        UserSummary userSummary=new UserSummary();
//        userSummary.setId(currentUser.getId());
        userSummary.setName(currentUser.getName());
        userSummary.setUsername(currentUser.getUsername());
        userSummary.setEmail(currentUser.getEmail());
        return userSummary;

    }
}

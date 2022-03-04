package com.ndirituedwin.Controller;

import com.ndirituedwin.Dto.responses.UserSummary;
import com.ndirituedwin.Service.UserService;
import com.ndirituedwin.security.CurrentUser;
import com.ndirituedwin.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
   private UserService userService;

    @GetMapping("/currentuser")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentuser(@CurrentUser UserPrincipal currentUser){
        return userService.getCurrentuser(currentUser);
    }
}
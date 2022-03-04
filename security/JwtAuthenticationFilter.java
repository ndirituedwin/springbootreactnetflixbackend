package com.ndirituedwin.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private  CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
           try {
               String jwt=getJwtFromRequest(request);
               log.info("getting the jwt from request {}",jwt);

               if(StringUtils.hasText(jwt)&& jwtTokenProvider.validateToken(jwt)){
                    Long userId=jwtTokenProvider.getUserIdFromJwt(jwt);
                   log.info("getting the userId from Jwt {}",userId);
                   UserDetails userDetails=customUserDetailsService.loadUserById(userId);
                   log.info("userDetails {}",userDetails);
                   UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                   log.info("logging the Useramepasswordauthentication {}",authentication);
                   authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(authentication);
               }
           }catch (Exception exception){
             log.error("could not set authentication in security context due to {}",exception.getMessage());
           }
           filterChain.doFilter(request,response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken=request.getHeader("Authorization");
    log.info("logging bearerToken {}",bearerToken);
    log.info("logging the request {}",request);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
    log.info("logging the bearerToken {} ",bearerToken);
        return bearerToken.substring(7,bearerToken.length());
    }
    return null;
    }

}

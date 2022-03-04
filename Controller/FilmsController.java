package com.ndirituedwin.Controller;

import com.ndirituedwin.Dto.FilmsRequest;
import com.ndirituedwin.Dto.responses.FilmsResponse;
import com.ndirituedwin.Dto.responses.PagedFilmsResponse;
import com.ndirituedwin.Dto.responses.PagedResponse;
import com.ndirituedwin.Service.FilmsService;
import com.ndirituedwin.Utils.AppConstants;
import com.ndirituedwin.security.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/films")
@Slf4j
@AllArgsConstructor
public class FilmsController {

    private final FilmsService filmsservice;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> savefilms(@Valid @RequestBody FilmsRequest filmsRequest, BindingResult result, @CurrentUser UserPrincipal currentUser){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            log.info("logging error map {}",errorMap);
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(filmsservice.save(filmsRequest,currentUser), HttpStatus.CREATED);
    }
    @GetMapping("/getall")
    @PreAuthorize("hasRole('ADMIN')or hasRole('USER')")
    public PagedFilmsResponse<FilmsResponse> getallfilms(@CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
    ){
        return filmsservice.getallfilms(currentUser,page,size);

    }

}

package com.ndirituedwin.Controller;

import com.ndirituedwin.Dto.SeriesRequest;
import com.ndirituedwin.Dto.responses.PagedResponse;
import com.ndirituedwin.Dto.responses.SeriesResponse;
import com.ndirituedwin.Service.SeriesService;
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
@RequestMapping("/api/series")
@Slf4j
@AllArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveseries(@Valid @RequestBody SeriesRequest seriesRequest, BindingResult result, @CurrentUser UserPrincipal currentUser){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            log.info("logging error map {}",errorMap);
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(seriesService.save(seriesRequest,currentUser), HttpStatus.CREATED);
    }
    @GetMapping("/getall")
    @PreAuthorize("hasRole('ADMIN')or hasRole('USER')")
    public PagedResponse<SeriesResponse> getallseries(@CurrentUser UserPrincipal currentUser,
                                                      @RequestParam(value = "page",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                      @RequestParam(value = "size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
    ){
        return seriesService.getallseries(currentUser,page,size);
    }


}

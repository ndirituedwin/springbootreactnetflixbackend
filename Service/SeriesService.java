package com.ndirituedwin.Service;

import com.ndirituedwin.Dto.SeriesRequest;
import com.ndirituedwin.Dto.responses.PagedResponse;
import com.ndirituedwin.Dto.responses.SeriesResponse;
import com.ndirituedwin.Entity.Series;
import com.ndirituedwin.Entity.User;
import com.ndirituedwin.Exception.BadRequestException;
import com.ndirituedwin.Repository.SeriesRepository;
import com.ndirituedwin.Repository.UserRepository;
import com.ndirituedwin.Utils.AppConstants;
import com.ndirituedwin.Utils.ModelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SeriesService {
    private final UserRepository userRepository;

    private final SeriesRepository seriesRepository;
    public SeriesResponse save(SeriesRequest seriesRequest, UserPrincipal currentUser) {
        Series series=new Series();
        series.setTitle(seriesRequest.getTitle());
        series.setDescription(seriesRequest.getDescription());
        series.setSlug(seriesRequest.getSlug());
        series.setMaturity(seriesRequest.getMaturity());
        series.setGenre(seriesRequest.getGenre());
        Series response=seriesRepository.save(series);
        return SeriesResponse.builder()
                .title(response.getTitle())
                .description(response.getDescription())
                .genre(response.getGenre())
                .maturity(response.getMaturity())
                .slug(response.getSlug())
                .build();


    }

    public PagedResponse<SeriesResponse> getallseries(UserPrincipal currentUser, int page, int size) {
                validatePageNumberandSize(page,size);
        Pageable pageable= PageRequest.of(page, size, Sort.Direction.DESC,"createdAt");
        log.info("logging pageable {}",pageable);
        Page<Series> series=seriesRepository.findAll(pageable);
        log.info("logging series {}",series);
        if (series.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(),series.getNumber(),series.getSize(),series.getTotalElements(),series.getTotalPages(),series.isLast());
        }
//        List<Series> series1=series.getContent();

        Map<Long,User> creatormap=getseriescreatormap(series.getContent());
        log.info("series creatormap {}",creatormap);
        List<SeriesResponse> series1=series.map(ser->{
          return ModelMapper.mapSeriesToSeriesResponse(ser,creatormap.get(ser.getCreatedBy()));
        }).getContent();
        return new PagedResponse<>(series1,series.getNumber(),series.getSize(),series.getTotalElements(),series.getTotalPages(),series.isLast());
    }
    private Map<Long,User> getseriescreatormap(List<Series> series){
        List<Long> creatorIds=series.stream().map(series1 -> series1.getCreatedBy()).distinct().collect(Collectors.toList());
        List<User> creators=userRepository.findByIdIn(creatorIds);
        Map<Long,User> creatorMap=creators.stream().collect(Collectors.toMap(User::getId,Function.identity()));
         log.info("logging creatormap {}",creatorMap);
        return creatorMap;
    }

    private void validatePageNumberandSize(int page, int size) {
          if (page<0){
              throw new BadRequestException("page number may not be less than zero ");
          }
        if (size> AppConstants.MAX_PAGE_SIZE){
            throw new BadRequestException("page size may not be greater than "+AppConstants.MAX_PAGE_SIZE);
        }
    }
}

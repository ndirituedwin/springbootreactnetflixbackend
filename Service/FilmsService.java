package com.ndirituedwin.Service;

import com.ndirituedwin.Dto.FilmsRequest;
import com.ndirituedwin.Dto.responses.FilmsResponse;
import com.ndirituedwin.Dto.responses.PagedFilmsResponse;
import com.ndirituedwin.Dto.responses.SeriesResponse;
import com.ndirituedwin.Entity.Films;
import com.ndirituedwin.Entity.Series;
import com.ndirituedwin.Entity.User;
import com.ndirituedwin.Exception.BadRequestException;
import com.ndirituedwin.Repository.FilmsRepository;
import com.ndirituedwin.Repository.UserRepository;
import com.ndirituedwin.Utils.AppConstants;
import com.ndirituedwin.Utils.ModelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class FilmsService {
    private final UserRepository userRepository;

    private final FilmsRepository filmsRepository;
    public FilmsResponse save(FilmsRequest filmsrequest, UserPrincipal currentUser) {
        Films films=new Films();
        films.setTitle(filmsrequest.getTitle());
        films.setDescription(filmsrequest.getDescription());
        films.setSlug(filmsrequest.getSlug());
        films.setMaturity(filmsrequest.getMaturity());
        films.setGenre(filmsrequest.getGenre());
        Films response=filmsRepository.save(films);
        return FilmsResponse.builder()
                .title(response.getTitle())
                .description(response.getDescription())
                .genre(response.getGenre())
                .maturity(response.getMaturity())
                .slug(response.getSlug())
                .build();


    }

    public PagedFilmsResponse<FilmsResponse> getallfilms(UserPrincipal currentUser, int page, int size) {
        validatePageNumberandSize(page,size);
        Pageable pageable= PageRequest.of(page,size, Sort.Direction.DESC,"createdAt");
        log.info("logging pageable {}",pageable);
        Page<Films> films=filmsRepository.findAll(pageable);
        log.info("logging al films {}",films);
        if (films.getNumberOfElements()==0){
            return new PagedFilmsResponse<>(Collections.emptyList(),films.getNumber(),films.getSize(),films.getTotalElements(),films.getTotalPages(),films.isLast());
        }
        Map<Long, User> creatormap=getfilmscreatormap(films.getContent());
        log.info("logging creatormap {}",creatormap);
        List<FilmsResponse> films1=films.map(film->{
            return ModelMapper.mapFilmsToFilmsResponse(film,creatormap.get(film.getCreatedBy()));
        }).getContent();
        log.info("logging fimI {}",films1);
        return new PagedFilmsResponse<>(films1,films.getNumber(),films.getSize(),films.getTotalElements(),films.getTotalPages(),films.isLast());
    }
    private Map<Long,User> getfilmscreatormap(List<Films> films){
        List<Long> creatorIds=films.stream().map(films1 -> films1.getCreatedBy()).distinct().collect(Collectors.toList());
        List<User> creators=userRepository.findByIdIn(creatorIds);
        Map<Long,User> creatorMap=creators.stream().collect(Collectors.toMap(User::getId, Function.identity()));
          log.info("get films creatormap {}",creatorMap);
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

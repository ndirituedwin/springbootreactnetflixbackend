package com.ndirituedwin.Utils;

import com.ndirituedwin.Dto.responses.FilmsResponse;
import com.ndirituedwin.Dto.responses.SeriesResponse;
import com.ndirituedwin.Dto.responses.UserSummary;
import com.ndirituedwin.Entity.Films;
import com.ndirituedwin.Entity.Series;
import com.ndirituedwin.Entity.User;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ModelMapper {

    public static SeriesResponse mapSeriesToSeriesResponse(Series series,User creator){
        SeriesResponse seriesResponse=new SeriesResponse();
        seriesResponse.setTitle(series.getTitle());
        seriesResponse.setDescription(series.getDescription());
        seriesResponse.setGenre(series.getGenre());
        seriesResponse.setMaturity(seriesResponse.getMaturity());
        seriesResponse.setSlug(series.getSlug());
        seriesResponse.setCreatedAt(series.getCreatedAt());
        UserSummary creatorsummary=new UserSummary();
        creatorsummary.setId(creator.getId());
        creatorsummary.setName(creator.getName());
        creatorsummary.setEmail(creator.getEmail());
        creatorsummary.setUsername(creator.getUsername());
        seriesResponse.setCreatedBy(creatorsummary);
        return seriesResponse;

    }
    public static FilmsResponse mapFilmsToFilmsResponse(Films films, User creator){
        FilmsResponse seriesResponse=new FilmsResponse();
        seriesResponse.setTitle(films.getTitle());
        seriesResponse.setDescription(films.getDescription());
        seriesResponse.setGenre(films.getGenre());
        seriesResponse.setMaturity(seriesResponse.getMaturity());
        seriesResponse.setSlug(films.getSlug());
        seriesResponse.setCreatedAt(films.getCreatedAt());
        UserSummary creatorsummary=new UserSummary();
        creatorsummary.setId(creator.getId());
        creatorsummary.setName(creator.getName());
        creatorsummary.setEmail(creator.getEmail());
        creatorsummary.setUsername(creator.getUsername());
        seriesResponse.setCreatedBy(creatorsummary);
        return seriesResponse;

    }

//    public static PollResponse mapPollToPollResponse(Poll poll, Map<Long,Long> choiVotesMap, User creator, Long userVote){
//        PollResponse pollResponse=new PollResponse();
//        pollResponse.setId(poll.getId());
//        pollResponse.setQuestion(poll.getQuestion());
//        pollResponse.setCreationDateTime(poll.getCreatedAt());
//        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
//        pollResponse.setIsExpired(poll.getExpirationDateTime().isBefore(Instant.now()));
////        pollResponse.setChoices();
//        List<ChoiceResponse> choiceResponses=
//                poll.getChoices().stream().map(choice -> {
//                    ChoiceResponse choiceResponse=new ChoiceResponse();
//                    choiceResponse.setId(choice.getId());
//                    choiceResponse.setText(choice.getText());
//                    if (choiVotesMap.containsKey(choice.getId())){
//                        choiceResponse.setVoteCount(choiVotesMap.get(choice.getId()));
//                    }else{
//                        choiceResponse.setVoteCount(0);
//                    }
//                    return choiceResponse;
//
//                }).collect(Collectors.toList());
//        pollResponse.setChoices(choiceResponses);
//        log.info("logging choiceresponse {}",choiceResponses);
//        UserSummary creatorSummary=new UserSummary();
//        creatorSummary.setId(creator.getId());
//        creatorSummary.setName(creator.getName());
//        creatorSummary.setUsername(creator.getUsername());
//        pollResponse.setCreatedBy(creatorSummary);
//        if (userVote !=null){
//            pollResponse.setSelectedChoice(userVote);
//        }
//        long totalVotes=pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
//        pollResponse.setTotalVotes(totalVotes);
//        return pollResponse;
//
//    }
}

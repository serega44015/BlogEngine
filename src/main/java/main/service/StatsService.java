package main.service;

import main.dto.api.response.StatisticResponse;
import main.model.Post;
import main.model.PostVote;
import main.model.repositories.GlobalSettingRepository;
import main.model.repositories.PostRepository;
import main.model.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

import static main.mappers.converter.DateConverter.dateToLong;
import static main.mappers.converter.ResultValue.*;

@Service
public class StatsService {
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final GlobalSettingRepository globalSettingRepository;

  public StatsService(
      UserRepository userRepository,
      PostRepository postRepository,
      GlobalSettingRepository globalSettingRepository) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.globalSettingRepository = globalSettingRepository;
  }

  public StatisticResponse getMyStatistics(Principal principal) {
    main.model.User currentUser = userRepository.findByEmail(principal.getName());
    List<Post> userPosts = userRepository.findByEmail(currentUser.getEmail()).getUserPosts();
    return getStatistics(userPosts);
  }

  public ResponseEntity<StatisticResponse> getAllStatistics(Principal principal) {
    main.model.User currentUser = userRepository.findByEmail(principal.getName());
    StatisticResponse statisticResponse = getStatistics(postRepository.findAll());
    String showStatistics = globalSettingRepository.findByCode(STATISTICS_IS_PUBLIC).getValue();

    if (currentUser.getIsModerator() == ZERO && showStatistics.equals(NO)) {
      return new ResponseEntity<>(statisticResponse, HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(statisticResponse, HttpStatus.OK);
  }

  private StatisticResponse getStatistics(List<Post> posts) {
    StatisticResponse statisticResponse = new StatisticResponse();
    Integer postCount = posts.size();
    Integer likeCount = ZERO;
    Integer dislikeCount = ZERO;
    Integer viewCount = ZERO;

    Long firstPublication = dateToLong(posts.stream().findFirst().get().getTime());

    for (Post p : posts) {
      for (PostVote pv : p.getPostVoteList()) {
        if (pv.getValue() == ONE) {
          likeCount++;
        }
        if (pv.getValue() == -ONE) {
          dislikeCount++;
        }
      }
      viewCount = viewCount + p.getViewCount();
    }

    statisticResponse.setPostsCount(postCount);
    statisticResponse.setLikesCount(likeCount);
    statisticResponse.setDislikesCount(dislikeCount);
    statisticResponse.setViewsCount(viewCount);
    statisticResponse.setFirstPublication(firstPublication);

    return statisticResponse;
  }
}

package main.service;

import main.dto.api.response.StatisticResponse;
import main.model.Post;
import main.model.PostVotes;
import main.model.repositories.GlobalSettingRepository;
import main.model.repositories.PostRepository;
import main.model.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

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
    main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();
    List<Post> userPosts = userRepository.findByEmail(currentUser.getEmail()).get().getUserPosts();

    return getStatistics(userPosts);
  }

  public ResponseEntity<StatisticResponse> getAllStatistics(Principal principal) {
    main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();
    StatisticResponse statisticResponse = getStatistics(postRepository.findAll());

    String showStatistics = globalSettingRepository.findByCode("STATISTICS_IS_PUBLIC").getValue();

    if (currentUser.getIsModerator() == 0 && showStatistics.equals("NO")) {
      return new ResponseEntity<>(statisticResponse, HttpStatus.UNAUTHORIZED);
    }

    return new ResponseEntity<>(statisticResponse, HttpStatus.OK);
  }

  private StatisticResponse getStatistics(List<Post> posts) {
    StatisticResponse statisticResponse = new StatisticResponse();
    Integer postCount = posts.size();
    Integer likeCount = 0;
    Integer dislikeCount = 0;
    Integer viewCount = 0;
    Long firstPublication = posts.get(0).getTime().getTimeInMillis() / 1000;

    // TODO подумать, как превратить в стрим
    for (Post p : posts) {
      for (PostVotes pv : p.getPostVotesList()) {
        if (pv.getValue() == 1) {
          likeCount++;
        }
        if (pv.getValue() == -1) {
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

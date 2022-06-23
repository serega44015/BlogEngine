package main.service;

import main.dto.api.request.LikeDislikeRequest;
import main.dto.api.response.LikeDislikeResponse;
import main.model.Post;
import main.model.PostVote;
import main.model.repositories.PostRepository;
import main.model.repositories.PostVoteRepository;
import main.model.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Service
public class LikeDislikeService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final PostVoteRepository postVoteRepository;
  private final Integer LIKE = 1;
  private final Integer DISLIKE = -1;

  public LikeDislikeService(
      UserRepository userRepository,
      PostRepository postRepository,
      PostVoteRepository postVoteRepository) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.postVoteRepository = postVoteRepository;
  }

  public LikeDislikeResponse getReactionPost(
      LikeDislikeRequest likeRequest, Principal principal, Boolean reaction) {
    LikeDislikeResponse likeResponse = new LikeDislikeResponse();
    main.model.User currentUser = userRepository.findByEmail(principal.getName());
    Post post = postRepository.findPostById(likeRequest.getPostId());
    PostVote postVote = postVoteRepository.findByPostAndUserId(post, currentUser.getId());

    if (Objects.isNull(postVote)) {
      postVote = new PostVote();
      postVote.setPost(post);
      postVote.setValue(reaction ? LIKE : DISLIKE);
      postVote.setUserId(currentUser.getId());
      postVote.setTime(LocalDateTime.now());
    } else if ((postVote.getValue() == LIKE && reaction.booleanValue())
        || (postVote.getValue() == DISLIKE && !reaction.booleanValue())) {
      likeResponse.setResult(false);
      return likeResponse;
    } else {
      postVote.setValue(postVote.getValue() == DISLIKE ? LIKE : DISLIKE);
    }

    postVoteRepository.save(postVote);
    likeResponse.setResult(true);
    return likeResponse;
  }
}

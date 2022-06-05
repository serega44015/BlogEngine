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
import java.util.Date;
import java.util.Objects;

@Service
public class LikeDislikeService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final PostVoteRepository postVoteRepository;

  public LikeDislikeService(
      UserRepository userRepository,
      PostRepository postRepository,
      PostVoteRepository postVoteRepository) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.postVoteRepository = postVoteRepository;
  }

  public LikeDislikeResponse getLikePost(LikeDislikeRequest likeRequest, Principal principal) {
    LikeDislikeResponse likeResponse = new LikeDislikeResponse();
    main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();
    Post post = postRepository.findPostById(likeRequest.getPostId());
    PostVote postVote = postVoteRepository.findByPostAndUserId(post, currentUser.getId());

    if (!Objects.isNull(postVote) && postVote.getValue() != 1) {
      Integer voteUserId = postVote.getUserId();
      Integer currentUserId = currentUser.getId();
      if ((voteUserId == currentUserId) && (postVote.getValue() == -1)) {
        postVote.setValue(1);
        postVoteRepository.save(postVote);
        likeResponse.setResult(true);
      }
    }

    if (Objects.isNull(postVote)) {
      postVote = new PostVote();
      postVote.setPost(post);
      postVote.setValue(1);
      postVote.setUserId(currentUser.getId());
      postVote.setTime(new Date());
      likeResponse.setResult(true);
      postVoteRepository.save(postVote);
      return likeResponse;
    }

    likeResponse.setResult(true);
    return likeResponse;
  }

  public LikeDislikeResponse getDislikePost(LikeDislikeRequest likeRequest, Principal principal) {
    LikeDislikeResponse likeResponse = new LikeDislikeResponse();
    main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();
    Post post = postRepository.findPostById(likeRequest.getPostId());
    PostVote postVote = postVoteRepository.findByPostAndUserId(post, currentUser.getId());

    if (!Objects.isNull(postVote) && postVote.getValue() != -1) {
      Integer voteUserId = postVote.getUserId();
      Integer currentUserId = currentUser.getId();
      if ((voteUserId == currentUserId) && (postVote.getValue() == 1)) {
        postVote.setValue(-1);
        postVoteRepository.save(postVote);
        likeResponse.setResult(true);
      }
    }

    if (Objects.isNull(postVote)) {
      postVote = new PostVote();
      postVote.setPost(post);
      postVote.setValue(-1);
      postVote.setUserId(currentUser.getId());
      postVote.setTime(new Date());
      likeResponse.setResult(true);
      postVoteRepository.save(postVote);
      return likeResponse;
    }

    likeResponse.setResult(true);
    return likeResponse;
  }
}

package main.service;

import main.dto.api.request.LikeDislikeRequest;
import main.dto.api.response.LikeDislikeResponse;
import main.model.Post;
import main.model.PostVotes;
import main.model.repositories.PostRepository;
import main.model.repositories.PostVotesRepository;
import main.model.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.Objects;

@Service
public class LikeDislikeService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final PostVotesRepository postVotesRepository;

  public LikeDislikeService(
      UserRepository userRepository,
      PostRepository postRepository,
      PostVotesRepository postVotesRepository) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.postVotesRepository = postVotesRepository;
  }

  public LikeDislikeResponse getLikePost(LikeDislikeRequest likeRequest, Principal principal) {
    LikeDislikeResponse likeResponse = new LikeDislikeResponse();
    main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();
    Post post = postRepository.findPostById(likeRequest.getPostId());
    PostVotes postVotes = postVotesRepository.findByPostAndUserId(post, currentUser.getId());

    if (!Objects.isNull(postVotes) && postVotes.getValue() != 1) {
      Integer voteUserId = postVotes.getUserId();
      Integer currentUserId = currentUser.getId();
      if ((voteUserId == currentUserId) && (postVotes.getValue() == -1)) {
        postVotes.setValue(1);
        postVotesRepository.save(postVotes);
        likeResponse.setResult(true);
      }
    }

    if (Objects.isNull(postVotes)) {
      postVotes = new PostVotes();
      postVotes.setPost(post);
      postVotes.setValue(1);
      postVotes.setUserId(currentUser.getId());
      postVotes.setTime(new Date());
      likeResponse.setResult(true);
      postVotesRepository.save(postVotes);
      return likeResponse;
    }

    likeResponse.setResult(true);
    return likeResponse;
  }

  public LikeDislikeResponse getDislikePost(LikeDislikeRequest likeRequest, Principal principal) {
    LikeDislikeResponse likeResponse = new LikeDislikeResponse();
    main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();
    Post post = postRepository.findPostById(likeRequest.getPostId());
    PostVotes postVotes = postVotesRepository.findByPostAndUserId(post, currentUser.getId());

    if (!Objects.isNull(postVotes) && postVotes.getValue() != -1) {
      Integer voteUserId = postVotes.getUserId();
      Integer currentUserId = currentUser.getId();
      if ((voteUserId == currentUserId) && (postVotes.getValue() == 1)) {
        postVotes.setValue(-1);
        postVotesRepository.save(postVotes);
        likeResponse.setResult(true);
      }
    }

    if (Objects.isNull(postVotes)) {
      postVotes = new PostVotes();
      postVotes.setPost(post);
      postVotes.setValue(-1);
      postVotes.setUserId(currentUser.getId());
      postVotes.setTime(new Date());
      likeResponse.setResult(true);
      postVotesRepository.save(postVotes);
      return likeResponse;
    }

    likeResponse.setResult(true);
    return likeResponse;
  }
}

package main.service;

import main.dto.api.request.ModerationRequest;
import main.dto.api.response.ModerationResponse;
import main.model.Post;
import main.model.enums.ModerationStatus;
import main.model.repositories.PostRepository;
import main.model.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static main.mappers.converter.ResultValue.ACCEPTED;
import static main.mappers.converter.ResultValue.ONE;

@Service
public class ModerateService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;

  public ModerateService(UserRepository userRepository, PostRepository postRepository) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
  }

  public ModerationResponse getModerator(ModerationRequest moderationRequest, Principal principal) {
    ModerationResponse moderationResponse = new ModerationResponse();
    main.model.User currentUser = userRepository.findByEmail(principal.getName());
    Post post = postRepository.findPostById(moderationRequest.getPostId());

    ModerationStatus status =
        moderationRequest.getDecision().equals(ACCEPTED)
            ? ModerationStatus.ACCEPTED
            : ModerationStatus.DECLINED;
    post.setModerationStatus(status);

    if (currentUser.getIsModerator() == ONE) {
      moderationResponse.setResult(true);
    } else {
      moderationResponse.setResult(false);
    }
    postRepository.save(post);
    return moderationResponse;
  }
}

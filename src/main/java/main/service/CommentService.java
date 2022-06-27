package main.service;

import main.dto.api.errorDto.ErrorCommentDto;
import main.dto.api.request.CommentRequest;
import main.dto.api.response.CommentResponse;
import main.mappers.PostCommentMapper;
import main.model.Post;
import main.model.PostComment;
import main.model.repositories.PostCommentRepository;
import main.model.repositories.PostRepository;
import main.model.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class CommentService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final PostCommentRepository postCommentRepository;
  private final PostCommentMapper postCommentMapper = PostCommentMapper.INSTANCE;

  public CommentService(
      UserRepository userRepository,
      PostRepository postRepository,
      PostCommentRepository postCommentRepository) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.postCommentRepository = postCommentRepository;
  }

  public ResponseEntity<CommentResponse> postComment(
      CommentRequest commentRequest, Principal principal) {
    main.model.User currentUser = userRepository.findByEmail(principal.getName());
    CommentResponse commentResponse = new CommentResponse();

    if (commentRequest.getText().isEmpty()) {
      ErrorCommentDto errorCommentDto = new ErrorCommentDto();
      errorCommentDto.setText("Поле для текста не может быть пустым");
      commentResponse.setErrors(errorCommentDto);
      commentResponse.setResult(false);
      return new ResponseEntity<>(commentResponse, HttpStatus.BAD_REQUEST);
    }
    Post post = postRepository.findPostById(commentRequest.getPostId());
    PostComment postComment =
        postCommentMapper.toPostComment(commentRequest, post, currentUser, LocalDateTime.now());
    postCommentRepository.save(postComment);
    commentResponse.setId(postComment.getId());
    return new ResponseEntity<>(commentResponse, HttpStatus.OK);
  }
}

package main.service;

import main.dto.api.request.CommentRequest;
import main.dto.api.response.CommentResponse;
import main.model.Post;
import main.model.PostComment;
import main.model.repositories.PostCommentRepository;
import main.model.repositories.PostRepository;
import main.model.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Calendar;
import java.util.TimeZone;

@Service
public class CommentService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final PostCommentRepository postCommentRepository;

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

    main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();

    CommentResponse commentResponse = new CommentResponse();
    PostComment postComment = new PostComment();
    int parentId = commentRequest.getParentId();
    int postId = commentRequest.getPostId();
    String commentText = commentRequest.getText();

    if (commentText.isEmpty()) {
      commentResponse.setId(1);
      commentResponse.setText("Поле для текста не может быть пустым");
      commentResponse.setResult(false);
      return new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.BAD_REQUEST);
    }

    Post post = postRepository.findPostById(postId);
    Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    postComment.setPost(post);
    postComment.setParentId(parentId);
    postComment.setUser(currentUser);
    postComment.setTime(currentTime);
    postComment.setText(commentText);
    postCommentRepository.save(postComment);
    commentResponse.setId(postComment.getId());

    return new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.OK);
  }
}

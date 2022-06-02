package main.service;

import main.dto.api.request.CommentRequest;
import main.dto.api.response.CommentResponse;
import main.model.Post;
import main.model.PostComments;
import main.model.repositories.PostRepository;
import main.model.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public CommentResponse postComment(CommentRequest commentRequest, Principal principal) {

        main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();

        CommentResponse commentResponse = new CommentResponse();
        PostComments postComment = new PostComments();

        int parentId = commentRequest.getParentId();
        int postId = commentRequest.getPostId();
        String commentText = commentRequest.getText();

        System.out.println("Приперлись?");
        Post post = postRepository.findPostById(100000);
        System.out.println("ы");
//        Optional<Posts> optionalPost = postRepository.findPostById(postId);
//        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//        if (optionalPost.isPresent()) {
//            postComment.setPosts(optionalPost.get());
//            postComment.setParentId(parentId);
//            postComment.setUser(currentUser);
//            postComment.setTime(currentTime.getTime());
//            postComment.setText(commentText);
//
//            postCommentRepository.save(postComment);
//            commentResponse.setId(postComment.getId());
//        }
        return commentResponse;
    }


}

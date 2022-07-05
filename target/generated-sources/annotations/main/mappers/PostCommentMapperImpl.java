package main.mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import main.dto.CommentDto;
import main.dto.UserCommentDto;
import main.dto.api.request.CommentRequest;
import main.model.Post;
import main.model.PostComment;
import main.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-05T14:20:12+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.15 (Amazon.com Inc.)"
)
public class PostCommentMapperImpl implements PostCommentMapper {

    @Override
    public List<CommentDto> toListCommentDTO(List<PostComment> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentDto> list = new ArrayList<CommentDto>( comments.size() );
        for ( PostComment postComment : comments ) {
            list.add( postCommentToCommentDto( postComment ) );
        }

        return list;
    }

    @Override
    public PostComment toPostComment(CommentRequest commentRequest, Post post, User userComment, LocalDateTime currentTime) {
        if ( commentRequest == null && post == null && userComment == null && currentTime == null ) {
            return null;
        }

        PostComment postComment = new PostComment();

        if ( commentRequest != null ) {
            postComment.setText( commentRequest.getText() );
            postComment.setParentId( commentRequest.getParentId() );
        }
        if ( post != null ) {
            postComment.setPost( post );
        }
        if ( userComment != null ) {
            postComment.setUser( userComment );
        }
        if ( currentTime != null ) {
            postComment.setTime( currentTime );
        }

        return postComment;
    }

    protected UserCommentDto userToUserCommentDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserCommentDto userCommentDto = new UserCommentDto();

        userCommentDto.setId( user.getId() );
        userCommentDto.setName( user.getName() );
        userCommentDto.setPhoto( user.getPhoto() );

        return userCommentDto;
    }

    protected CommentDto postCommentToCommentDto(PostComment postComment) {
        if ( postComment == null ) {
            return null;
        }

        CommentDto commentDto = new CommentDto();

        if ( postComment.getId() != null ) {
            commentDto.setId( postComment.getId() );
        }
        commentDto.setText( postComment.getText() );
        commentDto.setUser( userToUserCommentDto( postComment.getUser() ) );

        return commentDto;
    }
}

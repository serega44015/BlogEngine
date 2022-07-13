package main.mappers;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import main.dto.CommentDto;
import main.dto.UserCommentDto;
import main.dto.api.request.CommentRequest;
import main.mappers.converter.DateConverter;
import main.model.Post;
import main.model.PostComment;
import main.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-11T12:54:44+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.15 (Amazon.com Inc.)"
)
public class PostCommentMapperImpl implements PostCommentMapper {

    @Override
    public CommentDto toCommentDTO(PostComment postComment) {
        if ( postComment == null ) {
            return null;
        }

        CommentDto commentDto = new CommentDto();

        commentDto.setId( postComment.getId() );
        commentDto.setText( postComment.getText() );
        commentDto.setTimeStamp( DateConverter.dateToLong( postComment.getTime() ) );
        commentDto.setUser( userToUserCommentDto( postComment.getUser() ) );

        return commentDto;
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
}

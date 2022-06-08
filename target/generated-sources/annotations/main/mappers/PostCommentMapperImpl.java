package main.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import main.dto.CommentDto;
import main.dto.UserCommentDto;
import main.model.PostComment;
import main.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-08T13:08:42+0300",
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

    protected UserCommentDto userToUserCommentDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserCommentDto userCommentDto = new UserCommentDto();

        if ( user.getId() != null ) {
            userCommentDto.setId( user.getId() );
        }
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

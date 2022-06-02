package main.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import main.dto.CommentDTO;
import main.dto.UserCommentDTO;
import main.model.PostComments;
import main.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-02T18:27:06+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.14 (Amazon.com Inc.)"
)
public class PostCommentsMapperImpl implements PostCommentsMapper {

    @Override
    public List<CommentDTO> toListCommentDTO(List<PostComments> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentDTO> list = new ArrayList<CommentDTO>( comments.size() );
        for ( PostComments postComments : comments ) {
            list.add( postCommentsToCommentDTO( postComments ) );
        }

        return list;
    }

    protected UserCommentDTO userToUserCommentDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserCommentDTO userCommentDTO = new UserCommentDTO();

        userCommentDTO.setId( user.getId() );
        userCommentDTO.setName( user.getName() );
        userCommentDTO.setPhoto( user.getPhoto() );

        return userCommentDTO;
    }

    protected CommentDTO postCommentsToCommentDTO(PostComments postComments) {
        if ( postComments == null ) {
            return null;
        }

        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId( postComments.getId() );
        commentDTO.setText( postComments.getText() );
        commentDTO.setUser( userToUserCommentDTO( postComments.getUser() ) );

        return commentDTO;
    }
}

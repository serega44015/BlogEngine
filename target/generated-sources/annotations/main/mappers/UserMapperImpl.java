package main.mappers;

import javax.annotation.processing.Generated;
import main.dto.UserCommentDTO;
import main.dto.UserDTO;
import main.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-19T14:27:38+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.15 (Amazon.com Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setName( user.getName() );

        return userDTO;
    }

    @Override
    public UserCommentDTO toUserCommentDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserCommentDTO userCommentDTO = new UserCommentDTO();

        userCommentDTO.setId( user.getId() );
        userCommentDTO.setName( user.getName() );
        userCommentDTO.setPhoto( user.getPhoto() );

        return userCommentDTO;
    }
}

package main.mappers;

import javax.annotation.processing.Generated;
import main.dto.UserDto;
import main.dto.UserLoginDto;
import main.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-27T21:14:17+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        if ( user.getId() != null ) {
            userDto.setId( user.getId() );
        }
        userDto.setName( user.getName() );

        return userDto;
    }

    @Override
    public UserLoginDto toUserLoginDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserLoginDto userLoginDto = new UserLoginDto();

        userLoginDto.setId( user.getId() );
        userLoginDto.setName( user.getName() );
        userLoginDto.setPhoto( user.getPhoto() );
        userLoginDto.setEmail( user.getEmail() );

        return userLoginDto;
    }
}

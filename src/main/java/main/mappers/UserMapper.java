package main.mappers;

import main.dto.UserCommentDto;
import main.dto.UserDto;
import main.dto.UserLoginDto;
import main.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Named("toUserDTO")
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    UserDto toUserDTO(User user);

    @Named("toUserCommentDTO")
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "photo", source = "user.photo")
    UserCommentDto toUserCommentDTO(User user);

    @Named("toUserLoginDto")
    UserLoginDto toUserLoginDto(User user);


}

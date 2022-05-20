package main.mappers;

import main.dto.UserCommentDTO;
import main.dto.UserDTO;
import main.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    //target = postDTO, sourse = post
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Named("toUserDTO")
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    UserDTO toUserDTO(User user);

    @Named("toUserCommentDTO")
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "photo", source = "user.photo")
    UserCommentDTO toUserCommentDTO(User user);


}

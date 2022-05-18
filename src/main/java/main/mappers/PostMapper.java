package main.mappers;

import main.dto.PostsDTO;
import main.dto.UserDTO;
import main.mappers.converter.DateConverter;
import main.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = DateConverter.class)
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
    //target = postDTO, sourse = post
    @Mapping(target = "id", source = "post.id")//мб и няняда
    @Mapping(target = "timeStamp", source = "post.time", qualifiedByName = "convertRegDate")
    @Mapping(target = "title", source = "post.title")
    @Mapping(target = "announce", source = "post.text")
    PostsDTO toPostDTO(Post post);
}

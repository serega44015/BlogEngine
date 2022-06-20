package main.mappers;

import main.dto.CommentDto;
import main.dto.api.request.CommentRequest;
import main.mappers.converter.DateConverter;
import main.model.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(uses = {DateConverter.class, UserMapper.class})
public interface PostCommentMapper {

    PostCommentMapper INSTANCE = Mappers.getMapper(PostCommentMapper.class);

    @Named("toListCommentDTO")
    @Mapping(target = "id", source = "comments.id")
    @Mapping(target = "text", source = "comments.text")
    @Mapping(target = "timeStamp", source = "comments.time", qualifiedByName = "convertRegDate")
    @Mapping(target = "user", source = "comments.user", qualifiedByName = "toUserCommentDTO")
    List<CommentDto> toListCommentDTO(List<PostComment> comments);

}

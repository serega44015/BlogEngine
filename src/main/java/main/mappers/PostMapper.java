package main.mappers;

import main.dto.PostDto;
import main.dto.api.response.PostIdResponse;
import main.mappers.converter.DateConverter;
import main.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateConverter.class, UserMapper.class, PostCommentMapper.class})
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "timeStamp", source = "post.time", qualifiedByName = "convertRegDate")
    @Mapping(target = "userDTO", source = "post.user", qualifiedByName = "toUserDTO")
    @Mapping(target = "title", source = "post.title")
    @Mapping(target = "announce", source = "post.text")
    @Mapping(target = "likeCount", expression = "java(post.getLikesAmount())")
    @Mapping(target = "dislikeCount", expression = "java(post.getDislikesAmount())")
    @Mapping(target = "commentCount", expression = "java(post.getCommentCount())")
    @Mapping(target = "viewCount", source = "post.viewCount")
    PostDto toPostDTO(Post post);

    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "timeStamp", source = "post.time", qualifiedByName = "convertRegDate")
    @Mapping(target = "active", expression = "java(post.getIsActiveResult())")
    @Mapping(target = "userDto", source = "post.user", qualifiedByName = "toUserDTO")
    @Mapping(target = "title", source = "post.title")
    @Mapping(target = "text", source = "post.text")
    @Mapping(target = "likeCount", expression = "java(post.getLikesAmount())")
    @Mapping(target = "dislikeCount", expression = "java(post.getDislikesAmount())")
    @Mapping(target = "comments", source = "post.postCommentList", qualifiedByName = "toListCommentDTO")
    @Mapping(target = "tags", expression = "java(post.getTagNameList())")
	PostIdResponse toPostResponseById(Post post);

}

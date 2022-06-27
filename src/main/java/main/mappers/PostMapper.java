package main.mappers;

import main.dto.PostDto;
import main.dto.api.request.CreatePostRequest;
import main.dto.api.response.PostIdResponse;
import main.mappers.converter.DateConverter;
import main.mappers.converter.ResultValue;
import main.model.Post;
import main.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateConverter.class, UserMapper.class, PostCommentMapper.class, ResultValue.class})
public interface PostMapper {

  PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

  @Mapping(target = "id", source = "post.id")
  @Mapping(target = "timeStamp", source = "post.time", qualifiedByName = "convertDate")
  @Mapping(target = "userDTO", source = "post.user", qualifiedByName = "toUserDTO")
  @Mapping(target = "title", source = "post.title")
  @Mapping(target = "announce", source = "post.text")
  @Mapping(target = "likeCount", source = "post.postVoteList", qualifiedByName = "likesAmount")
  @Mapping(
      target = "dislikeCount",
      source = "post.postVoteList",
      qualifiedByName = "dislikesAmount")
  @Mapping(
      target = "commentCount",
      source = "post.postCommentList",
      qualifiedByName = "commentCount")
  @Mapping(target = "viewCount", source = "post.viewCount")
  PostDto toPostDTO(Post post);

  @Mapping(target = "id", source = "post.id")
  @Mapping(target = "timeStamp", source = "post.time", qualifiedByName = "convertDate")
  @Mapping(target = "active", source = "post.isActive", qualifiedByName = "postBoolIsActive")
  @Mapping(target = "userDto", source = "post.user", qualifiedByName = "toUserDTO")
  @Mapping(target = "title", source = "post.title")
  @Mapping(target = "text", source = "post.text")
  @Mapping(target = "likeCount", source = "post.postVoteList", qualifiedByName = "likesAmount")
  @Mapping(
      target = "dislikeCount",
      source = "post.postVoteList",
      qualifiedByName = "dislikesAmount")
  @Mapping(
      target = "comments",
      source = "post.postCommentList",
      qualifiedByName = "toListCommentDTO")
  @Mapping(target = "tags", source = "post.tagList", qualifiedByName = "tagNameList")
  PostIdResponse toPostResponseById(Post post);

  @Mapping(target = "id", source = "post.id")
  @Mapping(target = "title", source = "createPostRequest.title")
  @Mapping(target = "text", source = "createPostRequest.text")
  @Mapping(target = "moderatorId", source = "user.isModerator")
  @Mapping(target = "user", source = "user")
  @Mapping(target = "time", source = "createPostRequest.timestamp", qualifiedByName = "convertLong")
  @Mapping(
      target = "isActive",
      source = "createPostRequest.active",
      qualifiedByName = "postIntIsActive")
  @Mapping(target = "viewCount", defaultValue = "0")
  Post toAddOrUpdatePost(Post post, CreatePostRequest createPostRequest, User user);
}

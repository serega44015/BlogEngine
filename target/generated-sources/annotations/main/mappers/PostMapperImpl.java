package main.mappers;

import javax.annotation.processing.Generated;
import main.dto.PostDto;
import main.dto.api.response.PostIdResponse;
import main.mappers.converter.DateConverter;
import main.model.Post;
import org.mapstruct.factory.Mappers;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-20T16:17:12+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.15 (Amazon.com Inc.)"
)
public class PostMapperImpl implements PostMapper {

    private final DateConverter dateConverter = new DateConverter();
    private final UserMapper userMapper = Mappers.getMapper( UserMapper.class );
    private final PostCommentMapper postCommentMapper = Mappers.getMapper( PostCommentMapper.class );

    @Override
    public PostDto toPostDTO(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDto postDto = new PostDto();

        if ( post.getId() != null ) {
            postDto.setId( post.getId() );
        }
        postDto.setTimeStamp( dateConverter.convertRegDate( post.getTime() ) );
        postDto.setUserDTO( userMapper.toUserDTO( post.getUser() ) );
        postDto.setTitle( post.getTitle() );
        postDto.setAnnounce( post.getText() );
        if ( post.getViewCount() != null ) {
            postDto.setViewCount( post.getViewCount() );
        }

        postDto.setLikeCount( post.getLikesAmount() );
        postDto.setDislikeCount( post.getDislikesAmount() );
        postDto.setCommentCount( post.getCommentCount() );

        return postDto;
    }

    @Override
    public PostIdResponse toPostResponseById(Post post) {
        if ( post == null ) {
            return null;
        }

        PostIdResponse postIdResponse = new PostIdResponse();

        postIdResponse.setId( post.getId() );
        postIdResponse.setTimeStamp( dateConverter.convertRegDate( post.getTime() ) );
        postIdResponse.setUserDto( userMapper.toUserDTO( post.getUser() ) );
        postIdResponse.setTitle( post.getTitle() );
        postIdResponse.setText( post.getText() );
        postIdResponse.setComments( postCommentMapper.toListCommentDTO( post.getPostCommentList() ) );
        postIdResponse.setViewCount( post.getViewCount() );

        postIdResponse.setActive( post.getIsActiveResult() );
        postIdResponse.setLikeCount( post.getLikesAmount() );
        postIdResponse.setDislikeCount( post.getDislikesAmount() );
        postIdResponse.setTags( post.getTagNameList() );

        return postIdResponse;
    }
}

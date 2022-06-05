package main.mappers;

import javax.annotation.processing.Generated;
import main.dto.PostsDTO;
import main.dto.api.response.PostIdResponse;
import main.mappers.converter.DateConverter;
import main.model.Post;
import org.mapstruct.factory.Mappers;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-05T19:13:12+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.14 (Amazon.com Inc.)"
)
public class PostMapperImpl implements PostMapper {

    private final DateConverter dateConverter = new DateConverter();
    private final UserMapper userMapper = Mappers.getMapper( UserMapper.class );
    private final PostCommentsMapper postCommentsMapper = Mappers.getMapper( PostCommentsMapper.class );

    @Override
    public PostsDTO toPostDTO(Post post) {
        if ( post == null ) {
            return null;
        }

        PostsDTO postsDTO = new PostsDTO();

        postsDTO.setId( post.getId() );
        postsDTO.setTimeStamp( dateConverter.convertRegDate( post.getTime() ) );
        postsDTO.setUserDTO( userMapper.toUserDTO( post.getUser() ) );
        postsDTO.setTitle( post.getTitle() );
        postsDTO.setAnnounce( post.getText() );
        postsDTO.setViewCount( post.getViewCount() );

        postsDTO.setLikeCount( post.getLikesAmount() );
        postsDTO.setDislikeCount( post.getDislikesAmount() );
        postsDTO.setCommentCount( post.getCommentCount() );

        return postsDTO;
    }

    @Override
    public PostIdResponse toPostResponseById(Post post) {
        if ( post == null ) {
            return null;
        }

        PostIdResponse postIdResponse = new PostIdResponse();

        postIdResponse.setId( post.getId() );
        postIdResponse.setTimeStamp( dateConverter.convertRegDate( post.getTime() ) );
        postIdResponse.setUserDTO( userMapper.toUserDTO( post.getUser() ) );
        postIdResponse.setTitle( post.getTitle() );
        postIdResponse.setText( post.getText() );
        postIdResponse.setComments( postCommentsMapper.toListCommentDTO( post.getPostCommentsList() ) );
        postIdResponse.setViewCount( post.getViewCount() );

        postIdResponse.setActive( post.getIsActiveResult() );
        postIdResponse.setLikeCount( post.getLikesAmount() );
        postIdResponse.setDislikeCount( post.getDislikesAmount() );
        postIdResponse.setTags( post.getTagNameList() );

        return postIdResponse;
    }
}

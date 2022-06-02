package main.mappers;

import javax.annotation.processing.Generated;
import main.dto.PostsDTO;
import main.dto.api.response.PostsIdResponse;
import main.mappers.converter.DateConverter;
import main.model.Post;
import org.mapstruct.factory.Mappers;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-02T16:05:07+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.15 (Amazon.com Inc.)"
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
    public PostsIdResponse toPostResponseById(Post post) {
        if ( post == null ) {
            return null;
        }

        PostsIdResponse postsIdResponse = new PostsIdResponse();

        postsIdResponse.setId( post.getId() );
        postsIdResponse.setTimeStamp( dateConverter.convertRegDate( post.getTime() ) );
        postsIdResponse.setUserDTO( userMapper.toUserDTO( post.getUser() ) );
        postsIdResponse.setTitle( post.getTitle() );
        postsIdResponse.setText( post.getText() );
        postsIdResponse.setComments( postCommentsMapper.toListCommentDTO( post.getPostCommentsList() ) );
        postsIdResponse.setViewCount( post.getViewCount() );

        postsIdResponse.setActive( post.getIsActiveResult() );
        postsIdResponse.setLikeCount( post.getLikesAmount() );
        postsIdResponse.setDislikeCount( post.getDislikesAmount() );
        postsIdResponse.setTags( post.getTagNameList() );

        return postsIdResponse;
    }
}

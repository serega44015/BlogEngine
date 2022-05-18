package main.mappers;

import javax.annotation.processing.Generated;
import main.dto.PostsDTO;
import main.mappers.converter.DateConverter;
import main.model.Post;
import org.mapstruct.factory.Mappers;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-18T23:31:34+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.14 (Amazon.com Inc.)"
)
public class PostMapperImpl implements PostMapper {

    private final DateConverter dateConverter = new DateConverter();
    private final UserMapper userMapper = Mappers.getMapper( UserMapper.class );

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
}

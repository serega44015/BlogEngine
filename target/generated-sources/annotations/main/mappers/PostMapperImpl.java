package main.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import main.dto.PostDto;
import main.dto.api.request.CreatePostRequest;
import main.dto.api.response.PostIdResponse;
import main.mappers.converter.DateConverter;
import main.mappers.converter.ResultValue;
import main.model.Post;
import main.model.PostComment;
import main.model.PostVote;
import main.model.Tag;
import main.model.User;
import org.mapstruct.factory.Mappers;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-24T19:49:27+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
public class PostMapperImpl implements PostMapper {

    private final UserMapper userMapper = Mappers.getMapper( UserMapper.class );
    private final PostCommentMapper postCommentMapper = Mappers.getMapper( PostCommentMapper.class );
    private final ResultValue resultValue = new ResultValue();

    @Override
    public PostDto toPostDTO(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDto postDto = new PostDto();

        postDto.setId( post.getId() );
        postDto.setTimeStamp( DateConverter.dateToLong( post.getTime() ) );
        postDto.setUserDTO( userMapper.toUserDTO( post.getUser() ) );
        postDto.setTitle( post.getTitle() );
        postDto.setAnnounce( post.getText() );
        postDto.setLikeCount( resultValue.likesAmount( post.getPostVoteList() ) );
        postDto.setDislikeCount( resultValue.dislikesAmount( post.getPostVoteList() ) );
        postDto.setCommentCount( resultValue.commentCount( post.getPostCommentList() ) );
        postDto.setViewCount( post.getViewCount() );

        return postDto;
    }

    @Override
    public PostIdResponse toPostResponseById(Post post) {
        if ( post == null ) {
            return null;
        }

        PostIdResponse postIdResponse = new PostIdResponse();

        postIdResponse.setId( post.getId() );
        postIdResponse.setTimeStamp( DateConverter.dateToLong( post.getTime() ) );
        postIdResponse.setActive( resultValue.postBoolIsActive( post.getIsActive() ) );
        postIdResponse.setUserDto( userMapper.toUserDTO( post.getUser() ) );
        postIdResponse.setTitle( post.getTitle() );
        postIdResponse.setText( post.getText() );
        postIdResponse.setLikeCount( resultValue.likesAmount( post.getPostVoteList() ) );
        postIdResponse.setDislikeCount( resultValue.dislikesAmount( post.getPostVoteList() ) );
        postIdResponse.setComments( postCommentMapper.toListCommentDTO( post.getPostCommentList() ) );
        postIdResponse.setTags( resultValue.tagNameList( post.getTagList() ) );
        postIdResponse.setViewCount( post.getViewCount() );

        return postIdResponse;
    }

    @Override
    public Post toAddOrUpdatePost(Post post, CreatePostRequest createPostRequest, User user) {
        if ( post == null && createPostRequest == null && user == null ) {
            return null;
        }

        Post post1 = new Post();

        if ( post != null ) {
            post1.setId( post.getId() );
            if ( post.getViewCount() != null ) {
                post1.setViewCount( post.getViewCount() );
            }
            else {
                post1.setViewCount( 0 );
            }
            post1.setModerationStatus( post.getModerationStatus() );
            List<PostVote> list = post.getPostVoteList();
            if ( list != null ) {
                post1.setPostVoteList( new ArrayList<PostVote>( list ) );
            }
            List<PostComment> list1 = post.getPostCommentList();
            if ( list1 != null ) {
                post1.setPostCommentList( new ArrayList<PostComment>( list1 ) );
            }
            List<Tag> list2 = post.getTagList();
            if ( list2 != null ) {
                post1.setTagList( new ArrayList<Tag>( list2 ) );
            }
        }
        if ( createPostRequest != null ) {
            post1.setTitle( createPostRequest.getTitle() );
            post1.setText( createPostRequest.getText() );
            post1.setTime( DateConverter.longToDate( createPostRequest.getTimestamp() ) );
            post1.setIsActive( resultValue.postIntIsActive( createPostRequest.getActive() ) );
        }
        if ( user != null ) {
            post1.setModeratorId( user.getIsModerator() );
            post1.setUser( user );
        }

        return post1;
    }
}

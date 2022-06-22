package main.mappers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.processing.Generated;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import main.dto.CommentDto;
import main.dto.UserCommentDto;
import main.dto.api.request.CommentRequest;
import main.model.Post;
import main.model.PostComment;
import main.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-22T22:05:08+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
public class PostCommentMapperImpl implements PostCommentMapper {

    private final DatatypeFactory datatypeFactory;

    public PostCommentMapperImpl() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        }
        catch ( DatatypeConfigurationException ex ) {
            throw new RuntimeException( ex );
        }
    }

    @Override
    public List<CommentDto> toListCommentDTO(List<PostComment> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentDto> list = new ArrayList<CommentDto>( comments.size() );
        for ( PostComment postComment : comments ) {
            list.add( postCommentToCommentDto( postComment ) );
        }

        return list;
    }

    @Override
    public PostComment toPostComment(CommentRequest commentRequest, Post post, User user, Calendar currentTime) {
        if ( commentRequest == null && post == null && user == null && currentTime == null ) {
            return null;
        }

        PostComment postComment = new PostComment();

        if ( commentRequest != null ) {
            postComment.setText( commentRequest.getText() );
            postComment.setParentId( commentRequest.getParentId() );
        }
        if ( post != null ) {
            postComment.setPost( post );
            postComment.setUser( post.getUser() );
        }
        if ( currentTime != null ) {
            postComment.setTime( xmlGregorianCalendarToLocalDateTime( calendarToXmlGregorianCalendar( currentTime ) ) );
        }

        return postComment;
    }

    private static LocalDateTime xmlGregorianCalendarToLocalDateTime( XMLGregorianCalendar xcal ) {
        if ( xcal == null ) {
            return null;
        }

        if ( xcal.getYear() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getMonth() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getDay() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getHour() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getMinute() != DatatypeConstants.FIELD_UNDEFINED
        ) {
            if ( xcal.getSecond() != DatatypeConstants.FIELD_UNDEFINED
                && xcal.getMillisecond() != DatatypeConstants.FIELD_UNDEFINED ) {
                return LocalDateTime.of(
                    xcal.getYear(),
                    xcal.getMonth(),
                    xcal.getDay(),
                    xcal.getHour(),
                    xcal.getMinute(),
                    xcal.getSecond(),
                    Duration.ofMillis( xcal.getMillisecond() ).getNano()
                );
            }
            else if ( xcal.getSecond() != DatatypeConstants.FIELD_UNDEFINED ) {
                return LocalDateTime.of(
                    xcal.getYear(),
                    xcal.getMonth(),
                    xcal.getDay(),
                    xcal.getHour(),
                    xcal.getMinute(),
                    xcal.getSecond()
                );
            }
            else {
                return LocalDateTime.of(
                    xcal.getYear(),
                    xcal.getMonth(),
                    xcal.getDay(),
                    xcal.getHour(),
                    xcal.getMinute()
                );
            }
        }
        return null;
    }

    private XMLGregorianCalendar calendarToXmlGregorianCalendar( Calendar cal ) {
        if ( cal == null ) {
            return null;
        }

        GregorianCalendar gcal = new GregorianCalendar( cal.getTimeZone() );
        gcal.setTimeInMillis( cal.getTimeInMillis() );
        return datatypeFactory.newXMLGregorianCalendar( gcal );
    }

    protected UserCommentDto userToUserCommentDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserCommentDto userCommentDto = new UserCommentDto();

        userCommentDto.setId( user.getId() );
        userCommentDto.setName( user.getName() );
        userCommentDto.setPhoto( user.getPhoto() );

        return userCommentDto;
    }

    protected CommentDto postCommentToCommentDto(PostComment postComment) {
        if ( postComment == null ) {
            return null;
        }

        CommentDto commentDto = new CommentDto();

        if ( postComment.getId() != null ) {
            commentDto.setId( postComment.getId() );
        }
        commentDto.setText( postComment.getText() );
        commentDto.setUser( userToUserCommentDto( postComment.getUser() ) );

        return commentDto;
    }
}

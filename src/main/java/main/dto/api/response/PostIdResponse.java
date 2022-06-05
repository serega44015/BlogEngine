package main.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.dto.CommentDto;
import main.dto.UserDto;

import java.util.List;

@Data
public class PostIdResponse {

  private Integer id;

  @JsonProperty("timestamp")
  private Long timeStamp;

  private Boolean active;

  @JsonProperty("user")
  private UserDto userDto;

  private String title;
  private String text;
  private Integer likeCount;
  private Integer dislikeCount;
  private Integer viewCount;
  private List<CommentDto> comments;
  private List<String> tags;
}

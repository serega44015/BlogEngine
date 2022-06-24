package main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostDto {

  private Integer id;

  @JsonProperty("timestamp")
  private Long timeStamp;

  @JsonProperty("user")
  private UserDto userDTO;

  private String title;
  private String announce;
  private Integer likeCount;
  private Integer dislikeCount;
  private Integer commentCount;
  private Integer viewCount;
}

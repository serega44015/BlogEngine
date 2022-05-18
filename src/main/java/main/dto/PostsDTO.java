package main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostsDTO {

    private int id;

    @JsonProperty("timestamp")
    private long timeStamp;

    @JsonProperty("user")
    private UserDTO userDTO;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;

}

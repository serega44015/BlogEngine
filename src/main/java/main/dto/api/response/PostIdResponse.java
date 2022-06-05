package main.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.dto.CommentDTO;
import main.dto.UserDTO;

import java.util.List;

@Data
public class PostIdResponse {

    private int id;

    @JsonProperty("timestamp")
    private long timeStamp;

    private boolean active;

    @JsonProperty("user")
    private UserDTO userDTO;

    private String title;
    private String text;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private List<CommentDTO> comments;
    private List<String> tags;



}

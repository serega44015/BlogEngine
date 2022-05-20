package main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentDTO {

    private int id;

    @JsonProperty("timestamp")
    private long timeStamp;

    private String text;
    private UserCommentDTO user;

}

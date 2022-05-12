package main.dto;

import lombok.Data;

@Data
public class CommentDTO {

    private int id;
    private long timestamp;
    private String text;
    private UserCommentDTO user;

}

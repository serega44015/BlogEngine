package main.dto;

import lombok.Data;

@Data
public class UserLoginDTO {

    private int id;
    private String name;
    private String photo;
    private String email;
    private boolean moderation;
    private int moderationCount;
    private boolean settings;

}

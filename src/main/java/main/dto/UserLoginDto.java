package main.dto;

import lombok.Data;

@Data
public class UserLoginDto {

  private Integer id;
  private String name;
  private String photo;
  private String email;
  private Boolean moderation;
  private Integer moderationCount;
  private Boolean settings;
}

package main.dto;

import lombok.Data;

@Data
public class ErrorDto {

  private String email;
  private String name;
  private String password;
  private String captcha;
}

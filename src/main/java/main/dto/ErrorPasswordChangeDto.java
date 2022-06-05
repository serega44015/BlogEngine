package main.dto;

import lombok.Data;

@Data
public class ErrorPasswordChangeDto {

  private String code;
  private String password;
  private String captcha;
}

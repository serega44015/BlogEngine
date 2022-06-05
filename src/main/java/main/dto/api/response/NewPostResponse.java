package main.dto.api.response;

import lombok.Data;
import main.dto.ErrorNewPostDto;

@Data
public class NewPostResponse {

  private Boolean result;
  private ErrorNewPostDto errors;
}

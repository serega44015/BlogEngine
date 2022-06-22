package main.dto.api.response;

import lombok.Data;
import main.dto.ErrorCreatePostDto;

@Data
public class CreatePostResponse {

  private Boolean result;
  private ErrorCreatePostDto errors;
}

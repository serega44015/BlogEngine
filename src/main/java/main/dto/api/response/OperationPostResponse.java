package main.dto.api.response;

import lombok.Data;
import main.dto.api.errorDto.ErrorCreatePostDto;

@Data
public class OperationPostResponse {

  private Boolean result;
  private ErrorCreatePostDto errors;
}

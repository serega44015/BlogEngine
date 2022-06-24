package main.dto.api.response;

import lombok.Data;
import main.dto.api.errorDto.ErrorCommentDto;

@Data
public class CommentResponse {

  private Integer id;
  private Boolean result;
  private ErrorCommentDto errors;
}

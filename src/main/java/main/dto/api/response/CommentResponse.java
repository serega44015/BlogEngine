package main.dto.api.response;

import lombok.Data;

@Data
public class CommentResponse {

  private Integer id;
  private Boolean result;
  private String text;
}

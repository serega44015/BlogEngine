package main.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.dto.PostDto;

import java.util.List;

@Data
public class PostResponse {

  private Long count;

  @JsonProperty("posts")
  private List<PostDto> postDTO;
}

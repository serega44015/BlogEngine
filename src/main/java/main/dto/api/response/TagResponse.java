package main.dto.api.response;

import lombok.Data;
import main.dto.TagDto;

import java.util.List;

@Data
public class TagResponse {

  private List<TagDto> tags;
}

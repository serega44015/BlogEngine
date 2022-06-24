package main.dto.api.response;

import lombok.Data;
import main.dto.api.errorDto.ErrorProfileDto;

@Data
public class ProfileResponse {

  private Boolean result;
  private ErrorProfileDto errors;
}

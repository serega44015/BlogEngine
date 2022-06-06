package main.dto.api.response;

import lombok.Data;
import main.dto.ErrorProfileDto;

@Data
public class ProfileResponse {

  private Boolean result;
  private ErrorProfileDto errors;
}

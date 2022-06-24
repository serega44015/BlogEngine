package main.dto.api.response;

import lombok.Data;
import main.dto.api.errorDto.ErrorPasswordChangeDto;

@Data
public class PasswordChangeResponse {

  private Boolean result;
  private ErrorPasswordChangeDto errors;
}

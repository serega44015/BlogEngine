package main.dto.api.response;

import lombok.Data;
import main.dto.api.errorDto.ErrorProfilePhotoDto;

@Data
public class PhotoProfileResponse {

  private Boolean result;
  private ErrorProfilePhotoDto errors;
}

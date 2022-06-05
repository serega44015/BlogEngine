package main.dto.api.response;

import lombok.Data;
import main.dto.ErrorProfilePhotoDto;

@Data
public class PhotoProfileResponse {

  private Boolean result;
  private ErrorProfilePhotoDto errors;
}

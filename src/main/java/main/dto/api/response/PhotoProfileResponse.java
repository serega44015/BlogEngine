package main.dto.api.response;

import lombok.Data;
import main.dto.ErrorsProfilePhotoDTO;

@Data
public class PhotoProfileResponse {

    private Boolean result;
    private ErrorsProfilePhotoDTO errors;
}

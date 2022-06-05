package main.dto.api.response;

import lombok.Data;
import main.dto.ErrorProfilePhotoDTO;

@Data
public class PhotoProfileResponse {

    private Boolean result;
    private ErrorProfilePhotoDTO errors;
}

package main.dto.api.response;

import lombok.Data;
import main.dto.ErrorsNewPostDTO;

@Data
public class NewPostResponse {

    private Boolean result;
    private ErrorsNewPostDTO errors;
}

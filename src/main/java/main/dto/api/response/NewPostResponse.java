package main.dto.api.response;

import lombok.Data;
import main.dto.ErrorNewPostDTO;

@Data
public class NewPostResponse {

    private Boolean result;
    private ErrorNewPostDTO errors;
}

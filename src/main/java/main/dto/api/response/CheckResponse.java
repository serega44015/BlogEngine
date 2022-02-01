package main.dto.api.response;

import lombok.Data;
import main.dto.UserLoginDTO;


@Data
public class CheckResponse {

    private boolean result;
    private UserLoginDTO user;


}

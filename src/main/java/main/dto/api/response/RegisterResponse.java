package main.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.dto.ErrorsDTO;

@Data
public class RegisterResponse {

    private boolean result;
    @JsonProperty("errors")
    private ErrorsDTO errorsDTO;


}

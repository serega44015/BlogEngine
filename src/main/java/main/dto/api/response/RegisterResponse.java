package main.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.dto.ErrorDto;

@Data
public class RegisterResponse {

  private Boolean result;

  @JsonProperty("errors")
  private ErrorDto errorDTO;
}

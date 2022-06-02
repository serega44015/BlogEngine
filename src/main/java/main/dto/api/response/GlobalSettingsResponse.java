package main.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GlobalSettingsResponse {
    @JsonProperty("MULTIUSER_MODE")
    private boolean multimuserMode;

    @JsonProperty("POST_PREMODERATION")
    private boolean postPremoderation;

    @JsonProperty("STATISTICS_IS_PUBLIC")
    private boolean statisticsIsPublic;


}

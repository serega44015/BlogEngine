package main.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.dto.TagsDTO;
import java.util.List;

@Data
public class TagsResponse {

    @JsonProperty("tags")
    private List<TagsDTO> tagsDTO;

}

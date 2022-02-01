package main.dto.api.response;

import lombok.Data;
import main.dto.TagsDTO;
import java.util.List;

@Data
public class TagsResponse {

    private List<TagsDTO> tagsDTO;

}

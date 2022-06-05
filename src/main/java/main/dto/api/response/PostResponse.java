package main.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.dto.PostsDTO;

import java.util.List;

@Data
public class PostResponse {

    private long count;

    @JsonProperty("posts")
    private List<PostsDTO> postsDTO;


}

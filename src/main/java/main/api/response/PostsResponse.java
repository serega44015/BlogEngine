package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.dto.PostsDTO;

import java.util.List;

@Data
public class PostsResponse {

    private int count;

    @JsonProperty("posts")
    private List<PostsDTO> postsDTO;


}

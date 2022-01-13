package main.api.response;

import lombok.Data;
import main.dto.PostsDTO;

import java.util.List;

@Data
public class PostsResponse {

    private int count;
    private List<PostsDTO> postsDTO;

}

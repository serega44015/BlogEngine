package main.service;

import main.api.response.PostsResponse;
import main.dto.PostsDTO;
import main.dto.UserDTO;
import main.model.Posts;
import main.model.repositories.PostsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostsService {

//    private final PostsRepository postsRepository;

    private Sort sort;
    private int countPosts;

//    public PostsService(PostsRepository postsRepository) {
//        this.postsRepository = postsRepository;
//    }

    public PostsResponse getPosts(int offset, int limit, String mode){
        List<PostsDTO> postsDTOList = new ArrayList<>();
        PostsResponse postsResponse = new PostsResponse();

//        Optional<Posts> optionalPosts = postsRepository.findById(1);
//        if(mode.equals("recent")){
//
//        }
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setId(345);
        postsDTO.setTimeStamp(1592338706);

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Дмитрий Петров");
        userDTO.setId(123);

        postsDTO.setUserDTO(userDTO);
        postsDTO.setTitle("Title");
        postsDTO.setAnnounce("Announce");
        postsDTO.setLikeCount(455);
        postsDTO.setDislikeCount(233);
        postsDTO.setCommentCount(555);
        postsDTO.setViewCount(111);

        postsDTOList.add(postsDTO);

        postsResponse.setCount(390);
        postsResponse.setPostsDTO(postsDTOList);
        return postsResponse;
    }
}

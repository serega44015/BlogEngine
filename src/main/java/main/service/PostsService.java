package main.service;

import main.api.response.PostsResponse;
import main.dto.PostsDTO;
import main.dto.UserDTO;
import main.model.Posts;
import main.model.User;
import main.model.repositories.PostsRepository;
import main.model.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

//    private Sort sort;
//    private int countPosts;

    public PostsService(PostsRepository postsRepository, UserRepository userRepository) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
    }


    public PostsResponse getPosts(int offset, int limit, String mode){
        List<PostsDTO> postsDTOList = new ArrayList<>();
        PostsResponse postsResponse = new PostsResponse();

        //Optional<Posts> postsOptional = postsRepository.findAllByUserId(1);
        List<Posts> postsList = postsRepository.findAll();
//        if(mode.equals("recent")){
//
//        }
        postsList.stream().forEach(el -> {
            System.out.println(el.getId() + "айди и " + el.getText());
        });

        for (int a = 0; a < postsList.size(); a++){
            PostsDTO postsDTO = new PostsDTO();
            postsDTO.setId(postsList.get(a).getId());
            postsDTO.setTimeStamp(1592338706); //потом взять из базы, переконвертировать время в секунды

            UserDTO userDTO = new UserDTO();
            Optional<User> optionalUser = userRepository.findUserById(postsList.get(a).getUserId().getId());

            userDTO.setName(optionalUser.get().getName());
            userDTO.setId(optionalUser.get().getId());
            postsDTO.setUserDTO(userDTO);

            postsDTO.setTitle(postsList.get(a).getTitle());
            postsDTO.setAnnounce(postsList.get(a).getText());
            postsDTO.setLikeCount(455);
            postsDTO.setDislikeCount(233);
            postsDTO.setCommentCount(555);
            postsDTO.setViewCount(111);

            postsDTOList.add(postsDTO);

        }

        postsResponse.setCount(390);
        postsResponse.setPostsDTO(postsDTOList);
        return postsResponse;
    }
}



/* PostsDTO postsDTO = new PostsDTO();
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
        postsResponse.setPostsDTO(postsDTOList);*/
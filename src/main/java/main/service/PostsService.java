package main.service;

import main.api.response.PostsResponse;
import main.dto.PostsDTO;
import main.dto.UserDTO;
import main.model.Posts;
import main.model.User;
import main.model.repositories.PostsRepository;
import main.model.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private Sort sort;

//    private Sort sort;
//    private int countPosts;

    public PostsService(PostsRepository postsRepository, UserRepository userRepository) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
    }


    public PostsResponse getPosts(int offset, int limit, String mode) {
        PostsResponse postsResponse = new PostsResponse();
        List<PostsDTO> postsDTOList = new ArrayList<>();

        if (mode.equals("recent")) {
            sort = Sort.by("time").descending();
            Page<Posts> postsPage = postsRepository.findAll(getSortedPaging(offset, limit, sort));
            List<Posts> listPostsRepository = postsPage.toList();
            postsDTOList = getListPostsDTO(listPostsRepository);

        }

        /*if (mode.equals("popular")){
            //sort = Sort.by("time").descending();
            Page<Posts> postsPage = postsRepository.findPostsOrderByLikes(getPaging(offset, limit)); похоже нужно через Query вытаскивать
            List<Posts> listPostsRepository = postsPage.toList();
            postsDTOList = getListPostsDTO(listPostsRepository);
        }*/

//        if (mode.equals("best")){
//
//        }

        /*if (mode.equals("early")){
            //early
            sort = Sort.by("time").ascending();
            Page<Posts> postsPage = postsRepository.findAll(getSortedPaging(offset, limit, sort));
            List<Posts> listPostsRepository = postsPage.toList();
            postsDTOList = getListPostsDTO(listPostsRepository);
        }*/

        postsResponse.setCount(390);
        postsResponse.setPostsDTO(postsDTOList);
        return postsResponse;
    }

    private Pageable getPaging(int offset, int limit) {
        //https://stackoverflow.com/questions/25008472/pagination-in-spring-data-jpa-limit-and-offset
        Pageable sortedPaging;
        int pageNumber = offset / limit;
        sortedPaging = PageRequest.of(pageNumber, limit);

        return sortedPaging;
    }

    private Pageable getSortedPaging(int offset, int limit, Sort sort) {
        //https://stackoverflow.com/questions/25008472/pagination-in-spring-data-jpa-limit-and-offset
        Pageable sortedPaging;
        int pageNumber = offset / limit;
        sortedPaging = PageRequest.of(pageNumber, limit, sort);

        return sortedPaging;
    }

    private List<PostsDTO> getListPostsDTO(List<Posts> listPostsRepository) {
        List<PostsDTO> postsDTOList = new ArrayList<>();
        for (int a = 0; a < listPostsRepository.size(); a++) {
            PostsDTO postsDTO = new PostsDTO();
            postsDTO.setId(listPostsRepository.get(a).getId());
            postsDTO.setTimeStamp(listPostsRepository.get(a).getTime().getTime()); //потом взять из базы, переконвертировать время в секунды
            long timer = listPostsRepository.get(a).getTime().getTime();
            UserDTO userDTO = new UserDTO();
            Optional<User> optionalUser = userRepository.findUserById(listPostsRepository.get(a).getUserId().getId());

            userDTO.setName(optionalUser.get().getName());
            userDTO.setId(optionalUser.get().getId());
            postsDTO.setUserDTO(userDTO);

            postsDTO.setTitle(listPostsRepository.get(a).getTitle());
            postsDTO.setAnnounce(listPostsRepository.get(a).getText());
            postsDTO.setLikeCount((int) (Math.random() * 1000) );
            postsDTO.setDislikeCount((int) (Math.random() * 1000) );
            postsDTO.setCommentCount((int) (Math.random() * 1000) );
            postsDTO.setViewCount((int) (Math.random() * 1000) );

            postsDTOList.add(postsDTO);

        }
        return postsDTOList;
    }
}

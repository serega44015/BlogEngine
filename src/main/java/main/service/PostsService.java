package main.service;

import main.dto.PostsDTO;
import main.dto.UserDTO;
import main.dto.api.response.PostsResponse;
import main.model.Post;
import main.model.User;
import main.model.repositories.PostRepository;
import main.model.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostsService {

    private final PostRepository postsRepository;
    private final UserRepository userRepository;

    public PostsService(PostRepository postsRepository, UserRepository userRepository) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
    }


    public PostsResponse getPostBySearch(int offset, int limit, String query) {

        List<Post> postsList = new ArrayList<>();
        PostsResponse postsResponse = new PostsResponse();

        if (query.isEmpty()) {
            postsList.addAll(
                    postsRepository.findAllPostsSortedByRecent(
                            getSortedPaging(offset, limit, Sort.by("time").descending())
                    ).toList()
            );
        } else {
            postsList.addAll(postsRepository.findPostsBySearch(
                    getPaging(offset, limit), query)
                    .toList()
            );
        }

        List<PostsDTO> postsDTOList = toPostDTOList(postsList);

        postsResponse.setCount(390);
        postsResponse.setPostsDTO(postsDTOList);

        return postsResponse;
    }


    public PostsResponse getPosts(int offset, int limit, String mode) {
        //List<PostsDTO> postsDTOList = new ArrayList<>();
        PostsResponse postsResponse = new PostsResponse();
        List<Post> postsList = new ArrayList<>();

        if (mode.equals("recent")) {
            postsList.addAll(
                    postsRepository.findAllPostsSortedByRecent(
                            getSortedPaging(offset, limit, Sort.by("time").descending())
                    ).toList()
            );
        }

        if (mode.equals("early")) {
            postsList.addAll(
                    postsRepository.findAllPostsSortedByRecent(
                            getSortedPaging(offset, limit, Sort.by("time").ascending())
                    ).toList()
            );
        }

        if (mode.equals("popular")) {
            postsList.addAll(
                    postsRepository.findAllPostOrderByComments(
                            getPaging(offset, limit)).toList()
            );
        }

        if (mode.equals("best")) {
            postsList.addAll(
                    postsRepository.findAllPostOrderByLikes(
                            getPaging(offset, limit)
                    ).toList()
            );
        }

        List<PostsDTO> postsDTOList = toPostDTOList(postsList);

        postsResponse.setCount(390);
        postsResponse.setPostsDTO(postsDTOList);
        return postsResponse;
    }

    private List<PostsDTO> toPostDTOList(List<Post> postsList){

        List<PostsDTO> postsDTOList = new ArrayList<>();

        for (int a = 0; a < postsList.size(); a++) {
            PostsDTO postsDTO = new PostsDTO();
            postsDTO.setId(postsList.get(a).getId());
            postsDTO.setTimeStamp(postsList.get(a).getTime().getTime()); //потом взять из базы, переконвертировать время в секунды

            UserDTO userDTO = new UserDTO();
            Optional<User> optionalUser = userRepository.findUserById(postsList.get(a).getUserId().getId());

            userDTO.setName(optionalUser.get().getName());
            userDTO.setId(optionalUser.get().getId());
            postsDTO.setUserDTO(userDTO);

            postsDTO.setTitle(postsList.get(a).getTitle());
            postsDTO.setAnnounce(postsList.get(a).getText());


            try {
                System.out.println("Лукойл " + postsDTOList.get(a).getLikeCount());
                postsDTO.setLikeCount(postsDTOList.get(a).getLikeCount());
            } catch (IndexOutOfBoundsException exception) {
                postsDTO.setLikeCount(333);
            }

            postsDTO.setDislikeCount(233);
            postsDTO.setCommentCount(555);
            postsDTO.setViewCount(111);

            postsDTOList.add(postsDTO);
        }

        return postsDTOList;
    }

    public Pageable getPaging(int offset, int limit) {

        // limit = itemPerPage
        // offset - это отступ от начала, с какого поста мы смотреть будем
        Pageable paging;
        int pageNumber = offset / limit;
        paging = PageRequest.of(pageNumber, limit);

        return paging;
    }

    public Pageable getSortedPaging(int offset, int limit, Sort sort) {
        Pageable sortedPaging;
        int pageNumber = offset / limit;
        sortedPaging = PageRequest.of(pageNumber, limit, sort);

        return sortedPaging;
    }
}



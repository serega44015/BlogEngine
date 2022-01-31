package main.service;

import main.api.response.PostsResponse;
import main.dto.PostsDTO;
import main.dto.UserDTO;
import main.model.PostVotes;
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

    public PostsService(PostsRepository postsRepository, UserRepository userRepository) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
    }


    public PostsResponse getPosts(int offset, int limit, String mode) {
        PostsResponse postsResponse = new PostsResponse();
        List<PostsDTO> postsDTOList = new ArrayList<>();

        if (mode.equals("recent")) {
            sort = Sort.by("time").descending();
            Page<Posts> postsPage = postsRepository.findAllPostsSortedByRecent(getSortedPaging(offset, limit, sort));
            postsDTOList = toPostsDTO(postsPage.toList());
            postsResponse.setCount((int) postsPage.getTotalElements());
            postsResponse.setPostsDTO(postsDTOList);
        }

        if (mode.equals("popular")) {
            //popular
            Page<Posts> postsPage = postsRepository.findAllPostOrderByComments(getPaging(offset, limit));
            postsDTOList = toPostsDTO(postsPage.toList());
            postsResponse.setCount((int) postsPage.getTotalElements());
            postsResponse.setPostsDTO(postsDTOList);
        }

        if (mode.equals("best")) {
            //best
            Page<Posts> postsPage = postsRepository.findAllPostOrderByLikes(getPaging(offset, limit));
            postsDTOList = toPostsDTO(postsPage.toList());
            postsResponse.setCount((int) postsPage.getTotalElements());
            postsResponse.setPostsDTO(postsDTOList);

        }

        if (mode.equals("early")) {
            //early
            sort = Sort.by("time").ascending();
            Page<Posts> postsPage = postsRepository.findAllPostsSortedByEarly(getSortedPaging(offset, limit, sort));
            postsDTOList = toPostsDTO(postsPage.toList());
            postsResponse.setCount((int) postsPage.getTotalElements());
            postsResponse.setPostsDTO(postsDTOList);
        }

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

    private List<PostsDTO> toPostsDTO(List<Posts> currentsPostsList) {
        List<PostsDTO> postsDTOList = new ArrayList<>();
        for (int a = 0; a < currentsPostsList.size(); a++) {
            Posts currentPost = currentsPostsList.get(a);
            PostsDTO postsDTO = new PostsDTO();
            postsDTO.setId(currentPost.getId());
            postsDTO.setTimeStamp(currentPost.getTime().getTime());
            UserDTO userDTO = new UserDTO();
            Optional<User> optionalUser = userRepository.findUserById(currentPost.getUserId().getId());

            userDTO.setName(optionalUser.get().getName());
            userDTO.setId(optionalUser.get().getId());
            postsDTO.setUserDTO(userDTO);

            postsDTO.setTitle(currentPost.getTitle());
            postsDTO.setAnnounce(currentPost.getText());

            int likePosts = 0;
            int disLikePosts = 0;
            for (PostVotes pv : currentPost.getPostVotesList()) {
                if (pv.getPostsId().getId() == currentPost.getId()) {
                    int value = pv.getValue();
                    if (value == 1) {
                        likePosts++;
                    }

                    if (value == -1) {
                        disLikePosts++;
                    }
                }
            }

            postsDTO.setLikeCount(likePosts);
            postsDTO.setDislikeCount(disLikePosts);
            postsDTO.setCommentCount(currentPost.getPostCommentsList().size());
            postsDTO.setViewCount((int) (Math.random() * 1000));

            postsDTOList.add(postsDTO);

        }
        return postsDTOList;
    }


}

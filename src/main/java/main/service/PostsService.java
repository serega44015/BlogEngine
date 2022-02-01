package main.service;

import main.dto.api.response.PostsResponse;
import main.dto.PostsDTO;
import main.dto.UserDTO;
import main.model.PostVotes;
import main.model.Post;
import main.model.User;
import main.model.repositories.PostRepository;
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

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final String recent = "recent";
    private final String popular = "popular";
    private final String best = "best";
    private final String early = "early";
    private Sort sort;

    public PostsService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    public PostsResponse getPosts(int offset, int limit, String mode) {
        PostsResponse postsResponse = new PostsResponse();
        List<PostsDTO> postsDTOList = new ArrayList<>();

        if (recent.equals(mode)) {
            //recent
            sort = Sort.by("time").descending();
            Page<Post> postsPage = postRepository.findAllPostsSortedByRecent(getSortedPaging(offset, limit, sort));
            postsDTOList = toPostsDTO(postsPage.toList());
            postsResponse.setCount((int) postsPage.getTotalElements());
            postsResponse.setPostsDTO(postsDTOList);
        }

        if (popular.equals(mode)) {
            //popular
            Page<Post> postsPage = postRepository.findAllPostOrderByComments(getPaging(offset, limit));
            postsDTOList = toPostsDTO(postsPage.toList());
            postsResponse.setCount((int) postsPage.getTotalElements());
            postsResponse.setPostsDTO(postsDTOList);
        }

        if (best.equals(mode)) {
            //best
            Page<Post> postsPage = postRepository.findAllPostOrderByLikes(getPaging(offset, limit));
            postsDTOList = toPostsDTO(postsPage.toList());
            postsResponse.setCount((int) postsPage.getTotalElements());
            postsResponse.setPostsDTO(postsDTOList);

        }

        if (early.equals(mode)) {
            //early
            sort = Sort.by("time").ascending();
            Page<Post> postsPage = postRepository.findAllPostsSortedByEarly(getSortedPaging(offset, limit, sort));
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

    private List<PostsDTO> toPostsDTO(List<Post> currentsPostList) {
        List<PostsDTO> postsDTOList = new ArrayList<>();
        for (int a = 0; a < currentsPostList.size(); a++) {
            Post currentPost = currentsPostList.get(a);
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
                if (pv.getPostId().getId() == currentPost.getId()) {
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

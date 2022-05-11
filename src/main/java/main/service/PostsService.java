package main.service;

import main.dto.PostsDTO;
import main.dto.UserDTO;
import main.dto.api.response.CalendarResponse;
import main.dto.api.response.PostsResponse;
import main.model.Post;
import main.model.User;
import main.model.repositories.PostRepository;
import main.model.repositories.TagRepository;
import main.model.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostsService {

    private final PostRepository postsRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public PostsService(PostRepository postsRepository, UserRepository userRepository, TagRepository tagRepository) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;

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

    private List<PostsDTO> toPostDTOList(List<Post> postsList) {

        List<PostsDTO> postsDTOList = new ArrayList<>();

        for (int a = 0; a < postsList.size(); a++) {
            Post post = postsList.get(a);
            int postId = post.getId();
            PostsDTO postsDTO = new PostsDTO();

            postsDTO.setId(postId);
            long times = post.getTime().getTime().getTime() / 1000; //на 3 часа отстают, потом додумать
            postsDTO.setTimeStamp(times);

            UserDTO userDTO = new UserDTO();
            Optional<User> optionalUser = userRepository.findUserById(post.getUserId().getId());

            userDTO.setName(optionalUser.get().getName());
            userDTO.setId(optionalUser.get().getId());
            postsDTO.setUserDTO(userDTO);

            postsDTO.setTitle(post.getTitle());
            postsDTO.setAnnounce(post.getText());

            Integer likes = postsRepository.countOfLikesPerPost(postId);
            Integer disLikes = postsRepository.countOfDisLikesPerPost(postId);
            Integer commentsCount = postsRepository.countOfCommentsPerComments(postId);

            postsDTO.setLikeCount(likes != null ? likes : 0);
            postsDTO.setDislikeCount(disLikes != null ? disLikes : 0);
            postsDTO.setCommentCount(commentsCount != null ? commentsCount : 0);
            postsDTO.setViewCount(post.getViewCount());

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

    public CalendarResponse getPostByYear(String year) {

        CalendarResponse calendarResponse = new CalendarResponse();
        TreeSet<Integer> years = postsRepository.getSetYearsByAllPosts();

        Map<String, Integer> posts = postsRepository.getDateFromPosts()
                .stream()
                .distinct()
                .collect(
                        Collectors.toMap(
                                date -> date,
                                date -> postsRepository.countPostsFromDate("%" + date + "%")
                        ));

        calendarResponse.setYears(years);
        calendarResponse.setPosts(posts);


        return calendarResponse;
    }

    public PostsResponse getPostByDate(int offset, int limit, String date) {
        PostsResponse postsResponse = new PostsResponse();

        List<PostsDTO> postsDTOList = toPostDTOList(
                postsRepository.findPostsByDate(getPaging(offset, limit), date).toList()
        );

        postsResponse.setCount(postsDTOList.size());
        postsResponse.setPostsDTO(postsDTOList);

        return postsResponse;

    }

    public PostsResponse getPostByTag(int offset, int limit, String tag){
        PostsResponse postsResponse = new PostsResponse();

        int tagId = tagRepository.findTagIdByName(tag);

        List<PostsDTO> postsDTOList = toPostDTOList(
                postsRepository.findPostsByTagId(getPaging(offset, limit), tagId).toList()
        );

        postsResponse.setCount(postsDTOList.size());
        postsResponse.setPostsDTO(postsDTOList);

        return postsResponse;

    }




}



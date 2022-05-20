package main.service;

import main.dto.api.response.CalendarResponse;
import main.dto.api.response.PostsIdResponse;
import main.dto.api.response.PostsResponse;
import main.mappers.PostCommentsMapper;
import main.mappers.PostMapper;
import main.model.Post;
import main.model.repositories.PostRepository;
import main.model.repositories.TagRepository;
import main.model.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class PostsService {

    private final PostRepository postsRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper = PostMapper.INSTANCE;
    private final PostCommentsMapper commentsMapper = PostCommentsMapper.INSTANCE;
    private final AuthenticationManager authenticationManager;

    public PostsService(PostRepository postsRepository, UserRepository userRepository, TagRepository tagRepository, AuthenticationManager authenticationManager) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.authenticationManager = authenticationManager;
    }


    public PostsResponse getPostBySearch(int offset, int limit, String query) {

        PostsResponse postsResponse = new PostsResponse();

        if (query.isEmpty()) {
            Page<Post> posts = postsRepository.findAllPostsSortedByRecent(
                    getSortedPaging(offset, limit, Sort.by("time").descending()));
            settersPostsResponse(postsResponse, posts);

        } else {
            Page<Post> posts = postsRepository.findPostsBySearch(
                    getPaging(offset, limit), query);
            settersPostsResponse(postsResponse, posts);
        }


        return postsResponse;
    }


    public PostsResponse getPosts(int offset, int limit, String mode) {
        PostsResponse postsResponse = new PostsResponse();

        if (mode.equals("recent")) {
            Page<Post> posts = postsRepository
                    .findAllPostsSortedByRecent(getSortedPaging(offset, limit, Sort.by("time").descending()));
            settersPostsResponse(postsResponse, posts);
        }


        if (mode.equals("early")) {
            Page<Post> posts = postsRepository
                    .findAllPostsSortedByRecent(getSortedPaging(offset, limit, Sort.by("time").ascending()));
            settersPostsResponse(postsResponse, posts);
        }


        if (mode.equals("popular")) {
            Page<Post> posts = postsRepository
                    .findAllPostOrderByComments(getPaging(offset, limit));
            settersPostsResponse(postsResponse, posts);
        }


        if (mode.equals("best")) {
            Page<Post> posts = postsRepository
                    .findAllPostOrderByLikes(getPaging(offset, limit));
            settersPostsResponse(postsResponse, posts);
        }

        return postsResponse;
    }

    private void settersPostsResponse(PostsResponse postsResponse, Page<Post> posts) {
        postsResponse.setPostsDTO(posts
                .stream()
                .map(postMapper::toPostDTO)
                .collect(Collectors.toList()));
        postsResponse.setCount(posts.getTotalElements());
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
        Page<Post> posts = postsRepository.findPostsByDate(getPaging(offset, limit), date);
        settersPostsResponse(postsResponse, posts);
        return postsResponse;
    }

    public PostsResponse getPostByTag(int offset, int limit, String tag) {
        PostsResponse postsResponse = new PostsResponse();
        int tagId = tagRepository.findTagIdByName(tag);

        Page<Post> posts = postsRepository.findPostsByTagId(getPaging(offset, limit), tagId);
        settersPostsResponse(postsResponse, posts);

        return postsResponse;
    }

    public PostsIdResponse getPostById(int id) {
        Post post;
        try {
            post = postsRepository.findPostsById(id).get();
        } catch (NoSuchElementException n) {
            n.getMessage();
            return null; //Если поста с данным id не существует, то возвращаем null, и контроллер выдаст HttpStatus.NOT_FOUND
        }

        return postMapper.toPostResponseById(post);
    }

    public PostsResponse getMyPosts(int offset, int limit, String status){
        PostsResponse postsResponse = new PostsResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        main.model.User currentUser = userRepository.findByEmail(user.getUsername()).get();
        Sort sort = Sort.by("time").descending();

        if (status.equals("inactive")) {
            Page<Post> posts = postsRepository
                    .findStatusInactiveByPosts(currentUser.getId(), getSortedPaging(offset, limit, sort));
            postsResponse.setCount(posts.getTotalElements());
            settersPostsResponse(postsResponse, posts);
        }

        if (status.equals("pending")) {
            Page<Post> posts = postsRepository
                    .findStatusPendingByPosts(currentUser.getId(), getSortedPaging(offset, limit, sort));
            postsResponse.setCount(posts.getTotalElements());
            settersPostsResponse(postsResponse, posts);
        }

        if (status.equals("declined")){
            Page<Post> posts = postsRepository
                    .findStatusDeclinedByPosts(currentUser.getId(), getSortedPaging(offset, limit, sort));
            postsResponse.setCount(posts.getTotalElements());
            settersPostsResponse(postsResponse, posts);
        }

        if (status.equals("published")){
            Page<Post> posts = postsRepository
                    .findStatusPublishedByPosts(currentUser.getId(), getSortedPaging(offset, limit, sort));
            postsResponse.setCount(posts.getTotalElements());
            settersPostsResponse(postsResponse, posts);
        }

        return postsResponse;
    }




}



package main.service;

import main.dto.ErrorsNewPostDTO;
import main.dto.api.request.NewPostRequest;
import main.dto.api.response.CalendarResponse;
import main.dto.api.response.NewPostResponse;
import main.dto.api.response.PostsIdResponse;
import main.dto.api.response.PostsResponse;
import main.mappers.PostCommentsMapper;
import main.mappers.PostMapper;
import main.model.Post;
import main.model.Tag;
import main.model.Tag2Post;
import main.model.enums.ModerationStatus;
import main.model.repositories.PostRepository;
import main.model.repositories.Tag2PostRepository;
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
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostsService {

    private final PostRepository postsRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final Tag2PostRepository tag2PostRepository;
    private final PostMapper postMapper = PostMapper.INSTANCE;
    private final PostCommentsMapper commentsMapper = PostCommentsMapper.INSTANCE;
    private final AuthenticationManager authenticationManager;

    public PostsService(PostRepository postsRepository, UserRepository userRepository, TagRepository tagRepository, Tag2PostRepository tag2PostRepository, AuthenticationManager authenticationManager) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.tag2PostRepository = tag2PostRepository;
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

    public PostsResponse getMyPosts(int offset, int limit, String status) {
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

        if (status.equals("declined")) {
            Page<Post> posts = postsRepository
                    .findStatusDeclinedByPosts(currentUser.getId(), getSortedPaging(offset, limit, sort));
            postsResponse.setCount(posts.getTotalElements());
            settersPostsResponse(postsResponse, posts);
        }

        if (status.equals("published")) {
            Page<Post> posts = postsRepository
                    .findStatusPublishedByPosts(currentUser.getId(), getSortedPaging(offset, limit, sort));
            postsResponse.setCount(posts.getTotalElements());
            settersPostsResponse(postsResponse, posts);
        }

        return postsResponse;
    }

    public PostsResponse getModerationPost(int offset, int limit, String status) {
        PostsResponse postsResponse = new PostsResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        main.model.User currentUser = userRepository.findByEmail(user.getUsername()).get();

        ModerationStatus moderationStatus = ModerationStatus.NEW;
        if (status.equals("accepted")) {
            moderationStatus = ModerationStatus.ACCEPTED;
        }

        if (status.equals("declined")) {
            moderationStatus = ModerationStatus.DECLINED;
        }

        Page<Post> posts = postsRepository
                .findModeratedPost(currentUser.getId(), moderationStatus,
                        getSortedPaging(offset, limit, Sort.by("time").descending()));

        postsResponse.setCount(posts.getTotalElements());
        settersPostsResponse(postsResponse, posts);

        return postsResponse;
    }

    public NewPostResponse addNewPost(@RequestBody NewPostRequest newPostRequest, Principal principal) {
        Integer MIN_TITLE_LENGTH = 3;
        Integer MIN_TEXT_LENGTH = 10;
        NewPostResponse newPostsResponse = new NewPostResponse();
        //TODO сделать, чтобы не возвращал Optional, а просто User
        main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();
        ErrorsNewPostDTO errorsNewPostDTO = new ErrorsNewPostDTO();

        System.out.println(newPostRequest.getTitle());
        System.out.println(newPostRequest.getActive());
        System.out.println(newPostRequest.getTags().toString());
        System.out.println(newPostRequest.getText());
        System.out.println(newPostRequest.getTimestamp());
        System.out.println("____________________________");

        String title = newPostRequest.getTitle();
        String text = newPostRequest.getText();
        newPostsResponse.setResult(false);
        if (title.length() < MIN_TITLE_LENGTH || title.isEmpty()) {
            errorsNewPostDTO.setTitle("Заголовок не установлен");
        } else if (text.length() < MIN_TEXT_LENGTH || text.isEmpty()) {
            errorsNewPostDTO.setText("Текст публикации слишком короткий");
        } else {
            newPostsResponse.setResult(true);
        }

        Post post = new Post();


        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(newPostRequest.getTimestamp());
        post.setTime(time);
        post.setIsActive(newPostRequest.getActive().equals(true) ? 1 : 0);
        post.setModeratorId(currentUser.getIsModerator());
        post.setUser(currentUser);
        post.setTime(time);
        post.setTitle(title);
        post.setText(text);
        post.setTagList(lookTag(newPostRequest.getTags(), post));


        newPostsResponse.setErrors(errorsNewPostDTO);
        return newPostsResponse;
    }


    private List<Tag> lookTag(List<String> tags, Post post) {
        List<Tag> tagList = new ArrayList<>();

        for (String tagName : tags) {
            Optional<Tag> tagOpt = tagRepository.findTagByName(tagName);
            if (tagOpt.isEmpty()) {
                Tag tag = new Tag();
                tag.setName(tagName);
                tagList.add(tag);
            } else {
                Tag repoTag = tagOpt.get();
                Tag2Post tag2Post = new Tag2Post();
                tag2Post.setTagId(repoTag.getId());
                tag2Post.setPostId(post.getId());
                tag2PostRepository.save(tag2Post);
            }
        }

        return tagList;
    }


}



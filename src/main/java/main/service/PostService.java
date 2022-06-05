package main.service;

import main.dto.ErrorNewPostDTO;
import main.dto.api.request.NewPostRequest;
import main.dto.api.response.CalendarResponse;
import main.dto.api.response.NewPostResponse;
import main.dto.api.response.PostIdResponse;
import main.dto.api.response.PostResponse;
import main.mappers.PostCommentsMapper;
import main.mappers.PostMapper;
import main.model.GlobalSetting;
import main.model.Post;
import main.model.Tag;
import main.model.Tag2Post;
import main.model.enums.ModerationStatus;
import main.model.repositories.*;
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
public class PostService {

    private final PostRepository postsRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final Tag2PostRepository tag2PostRepository;
    private final GlobalSettingRepository globalSettingRepository;
    private final PostMapper postMapper = PostMapper.INSTANCE;
    private final PostCommentsMapper commentsMapper = PostCommentsMapper.INSTANCE;
    private final AuthenticationManager authenticationManager;

    public PostService(PostRepository postsRepository, UserRepository userRepository, TagRepository tagRepository, Tag2PostRepository tag2PostRepository, GlobalSettingRepository globalSettingRepository, AuthenticationManager authenticationManager) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.tag2PostRepository = tag2PostRepository;
        this.globalSettingRepository = globalSettingRepository;
        this.authenticationManager = authenticationManager;
    }


    public PostResponse getPostBySearch(int offset, int limit, String query) {

        PostResponse postResponse = new PostResponse();

        if (query.isEmpty()) {
            Page<Post> posts = postsRepository.findAllPostsSortedByRecent(
                    getSortedPaging(offset, limit, Sort.by("time").descending()));
            settersPostsResponse(postResponse, posts);

        } else {
            Page<Post> posts = postsRepository.findPostsBySearch(
                    getPaging(offset, limit), query);
            settersPostsResponse(postResponse, posts);
        }


        return postResponse;
    }


    public PostResponse getPosts(int offset, int limit, String mode) {
        PostResponse postResponse = new PostResponse();

        if (mode.equals("recent")) {
            Page<Post> posts = postsRepository
                    .findAllPostsSortedByRecent(getSortedPaging(offset, limit, Sort.by("time").descending()));
            settersPostsResponse(postResponse, posts);
        }


        if (mode.equals("early")) {
            Page<Post> posts = postsRepository
                    .findAllPostsSortedByRecent(getSortedPaging(offset, limit, Sort.by("time").ascending()));
            settersPostsResponse(postResponse, posts);
        }


        if (mode.equals("popular")) {
            Page<Post> posts = postsRepository
                    .findAllPostOrderByComments(getPaging(offset, limit));
            settersPostsResponse(postResponse, posts);
        }


        if (mode.equals("best")) {
            Page<Post> posts = postsRepository
                    .findAllPostOrderByLikes(getPaging(offset, limit));
            settersPostsResponse(postResponse, posts);
        }

        return postResponse;
    }

    private void settersPostsResponse(PostResponse postResponse, Page<Post> posts) {
        postResponse.setPostsDTO(posts
                .stream()
                .map(postMapper::toPostDTO)
                .collect(Collectors.toList()));
        postResponse.setCount(posts.getTotalElements());
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

    public PostResponse getPostByDate(int offset, int limit, String date) {
        PostResponse postResponse = new PostResponse();
        Page<Post> posts = postsRepository.findPostsByDate(getPaging(offset, limit), date);
        settersPostsResponse(postResponse, posts);
        return postResponse;
    }

    public PostResponse getPostByTag(int offset, int limit, String tag) {
        PostResponse postResponse = new PostResponse();
        int tagId = tagRepository.findTagIdByName(tag);

        Page<Post> posts = postsRepository.findPostsByTagId(getPaging(offset, limit), tagId);
        settersPostsResponse(postResponse, posts);

        return postResponse;
    }

    public PostIdResponse getPostById(int id) {
        Post post;
        try {
            post = postsRepository.findPostById(id);
        } catch (NoSuchElementException n) {
            n.getMessage();
            return null; //Если поста с данным id не существует, то возвращаем null, и контроллер выдаст HttpStatus.NOT_FOUND
        }

        return postMapper.toPostResponseById(post);
    }

    public PostResponse getMyPosts(int offset, int limit, String status) {
        PostResponse postResponse = new PostResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        main.model.User currentUser = userRepository.findByEmail(user.getUsername()).get();
        Sort sort = Sort.by("time").descending();

        if (status.equals("inactive")) {
            Page<Post> posts = postsRepository
                    .findStatusInactiveByPosts(currentUser.getId(), getSortedPaging(offset, limit, sort));
            postResponse.setCount(posts.getTotalElements());
            settersPostsResponse(postResponse, posts);
        }

        if (status.equals("pending")) {
            Page<Post> posts = postsRepository
                    .findStatusPendingByPosts(currentUser.getId(), getSortedPaging(offset, limit, sort));
            postResponse.setCount(posts.getTotalElements());
            settersPostsResponse(postResponse, posts);
        }

        if (status.equals("declined")) {
            Page<Post> posts = postsRepository
                    .findStatusDeclinedByPosts(currentUser.getId(), getSortedPaging(offset, limit, sort));
            postResponse.setCount(posts.getTotalElements());
            settersPostsResponse(postResponse, posts);
        }

        if (status.equals("published")) {
            Page<Post> posts = postsRepository
                    .findStatusPublishedByPosts(currentUser.getId(), getSortedPaging(offset, limit, sort));
            postResponse.setCount(posts.getTotalElements());
            settersPostsResponse(postResponse, posts);
        }

        return postResponse;
    }

    public PostResponse getModerationPost(int offset, int limit, String status) {
        PostResponse postResponse = new PostResponse();
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

        postResponse.setCount(posts.getTotalElements());
        settersPostsResponse(postResponse, posts);

        return postResponse;
    }

    public NewPostResponse addNewPost(@RequestBody NewPostRequest newPostRequest, Principal principal) {
        Integer MIN_TITLE_LENGTH = 3;
        Integer MIN_TEXT_LENGTH = 10;
        NewPostResponse newPostsResponse = new NewPostResponse();
        //TODO сделать, чтобы не возвращал Optional, а просто User
        main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();
        ErrorNewPostDTO errorNewPostDTO = new ErrorNewPostDTO();

        String title = newPostRequest.getTitle();
        String text = newPostRequest.getText();
        newPostsResponse.setResult(false);
        if (title.length() < MIN_TITLE_LENGTH || title.isEmpty()) {
            errorNewPostDTO.setTitle("Заголовок не установлен");
        } else if (text.length() < MIN_TEXT_LENGTH || text.isEmpty()) {
            errorNewPostDTO.setText("Текст публикации слишком короткий");
        } else {
            newPostsResponse.setResult(true);
        }

        Post post = new Post();

        GlobalSetting postPremoderation = globalSettingRepository.findByCode("POST_PREMODERATION");
        if (postPremoderation.getValue().equals("YES")) {
            post.setModerationStatus(ModerationStatus.NEW);
        } else {
            post.setModerationStatus(ModerationStatus.ACCEPTED);
        }

        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        time.setTimeInMillis(newPostRequest.getTimestamp() * 1000);
        post.setTime(time);
        post.setIsActive(newPostRequest.getActive().equals(true) ? 1 : 0);
        post.setModeratorId(currentUser.getIsModerator());
        post.setUser(currentUser);
        post.setTime(time);
        post.setTitle(title);
        post.setText(text);
        postsRepository.save(post);
        post.setTagList(lookTag(newPostRequest.getTags(), post));
        postsRepository.save(post);
        newPostsResponse.setErrors(errorNewPostDTO);
        return newPostsResponse;
    }

    public NewPostResponse updatePost(Integer id, NewPostRequest newPostRequest, Principal principal) {
        //TODO убрать эти колхозные методы, сделать мапперы, из репозиториев нормально дёргаать
        // и попробовать соеденить с addNewPost, большинство одинаковой реализации
        Integer MIN_TITLE_LENGTH = 3;
        Integer MIN_TEXT_LENGTH = 10;
        NewPostResponse newPostsResponse = new NewPostResponse();
        //TODO сделать, чтобы не возвращал Optional, а просто User
        main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();
        ErrorNewPostDTO errorNewPostDTO = new ErrorNewPostDTO();

        String title = newPostRequest.getTitle();
        String text = newPostRequest.getText();


        newPostsResponse.setResult(false);
        if (title.length() < MIN_TITLE_LENGTH || title.isEmpty()) {
            errorNewPostDTO.setTitle("Заголовок не установлен");
        } else if (text.length() < MIN_TEXT_LENGTH || text.isEmpty()) {
            errorNewPostDTO.setText("Текст публикации слишком короткий");
        } else {
            newPostsResponse.setResult(true);
        }

        Post post = postsRepository.findPostById(id);
        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        post.setId(post.getId());
        post.setIsActive(newPostRequest.getActive().equals(true) ? 1 : 0);
        post.setTitle(title);
        post.setText(text);
        post.setUser(currentUser);
        post.setTagList(lookTag(newPostRequest.getTags(), post));
        post.setModeratorId(currentUser.getIsModerator());

        Long calendarTime = time.getTimeInMillis();
        Long requestTime = newPostRequest.getTimestamp() * 1000;
        if (requestTime > calendarTime){
            time.setTimeInMillis(requestTime);
        } else {
            time.setTimeInMillis(calendarTime);
        }
        post.setTime(time);

        boolean isAuthor = currentUser.getName().equals(post.getUser().getName());
        boolean isModerator = currentUser.getIsModerator() == 1;
        if (isAuthor) {
            post.setModerationStatus(ModerationStatus.NEW);
        }
        if (isModerator) {
            post.setModerationStatus(post.getModerationStatus());
        }
        postsRepository.save(post);
        newPostsResponse.setErrors(errorNewPostDTO);

        return newPostsResponse;
    }

    private List<Tag> lookTag(List<String> tags, Post post) {
        //TODO после настройки модерации поправить теги. Посмотреть, как они сохраняются в базу и узнать, попадают ли они все на пост
        List<Tag> tagList = new ArrayList<>();

        for (String tagName : tags) {
            Optional<Tag> tagOpt = tagRepository.findTagByName(tagName);
            if (tagOpt.isEmpty()) {
                Tag tag = new Tag();
                tag.setName(tagName);
                //TODO мб в else тоже нужен tagList.add
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



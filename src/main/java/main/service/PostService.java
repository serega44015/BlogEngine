package main.service;

import main.dto.ErrorNewPostDto;
import main.dto.api.request.NewPostRequest;
import main.dto.api.response.CalendarResponse;
import main.dto.api.response.NewPostResponse;
import main.dto.api.response.PostIdResponse;
import main.dto.api.response.PostResponse;
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
  private final Integer MIN_TITLE_LENGTH = 3;
  private final Integer MIN_TEXT_LENGTH = 10;

  public PostService(
      PostRepository postsRepository,
      UserRepository userRepository,
      TagRepository tagRepository,
      Tag2PostRepository tag2PostRepository,
      GlobalSettingRepository globalSettingRepository,
      AuthenticationManager authenticationManager) {
    this.postsRepository = postsRepository;
    this.userRepository = userRepository;
    this.tagRepository = tagRepository;
    this.tag2PostRepository = tag2PostRepository;
    this.globalSettingRepository = globalSettingRepository;
  }

  public PostResponse getPostBySearch(Integer offset, Integer limit, String query) {

    PostResponse postResponse = new PostResponse();

    Page<Post> posts;
    if (query.isEmpty()) {
      posts =
          postsRepository.findAllPostsSortedByRecentOrEarly(
              getSortedPaging(offset, limit, Sort.by("time").descending()));
    } else {
      posts = postsRepository.findPostsBySearch(getPaging(offset, limit), query);
    }
    settersPostsResponse(postResponse, posts);
    return postResponse;
  }

  public PostResponse getPosts(Integer offset, Integer limit, String mode) {
    PostResponse postResponse = new PostResponse();

    if (mode.equals("recent")) {
      Page<Post> posts =
          postsRepository.findAllPostsSortedByRecentOrEarly(
              getSortedPaging(offset, limit, Sort.by("time").descending()));
      settersPostsResponse(postResponse, posts);
    }

    if (mode.equals("early")) {
      Page<Post> posts =
          postsRepository.findAllPostsSortedByRecentOrEarly(
              getSortedPaging(offset, limit, Sort.by("time").ascending()));
      settersPostsResponse(postResponse, posts);
    }

    if (mode.equals("popular")) {
      Page<Post> posts = postsRepository.findAllPostOrderByComments(getPaging(offset, limit));
      settersPostsResponse(postResponse, posts);
    }

    if (mode.equals("best")) {
      Page<Post> posts = postsRepository.findAllPostOrderByLikes(getPaging(offset, limit));
      settersPostsResponse(postResponse, posts);
    }

    return postResponse;
  }

  private void settersPostsResponse(PostResponse postResponse, Page<Post> posts) {
    postResponse.setPostDTO(posts.stream().map(postMapper::toPostDTO).collect(Collectors.toList()));
    postResponse.setCount(posts.getTotalElements());
  }

  public Pageable getPaging(Integer offset, Integer limit) {
    // limit = itemPerPage
    // offset - это отступ от начала, с какого поста мы смотреть будем
    Pageable paging;
    Integer pageNumber = offset / limit;
    paging = PageRequest.of(pageNumber, limit);

    return paging;
  }

  public Pageable getSortedPaging(Integer offset, Integer limit, Sort sort) {
    Pageable sortedPaging;
    Integer pageNumber = offset / limit;
    sortedPaging = PageRequest.of(pageNumber, limit, sort);

    return sortedPaging;
  }

  public CalendarResponse getPostsByYear() {

    CalendarResponse calendarResponse = new CalendarResponse();
    TreeSet<Integer> years = postsRepository.getSetYearsByAllPosts();

    Map<String, Integer> posts =
        postsRepository.getDateFromPosts().stream()
            .distinct()
            .collect(
                Collectors.toMap(
                    date -> date, date -> postsRepository.countPostsFromDate("%" + date + "%")));

    calendarResponse.setYears(years);
    calendarResponse.setPosts(posts);
    return calendarResponse;
  }

  public PostResponse getPostByDate(Integer offset, Integer limit, String date) {
    PostResponse postResponse = new PostResponse();
    Page<Post> posts = postsRepository.findPostsByDate(getPaging(offset, limit), date);
    settersPostsResponse(postResponse, posts);
    return postResponse;
  }

  public PostResponse getPostByTag(Integer offset, Integer limit, String tag) {
    PostResponse postResponse = new PostResponse();
    Integer tagId = tagRepository.findByName(tag).getId();
    Page<Post> posts = postsRepository.findPostsByTagId(getPaging(offset, limit), tagId);
    settersPostsResponse(postResponse, posts);
    return postResponse;
  }

  public PostIdResponse getPostById(Integer id) {
    Post post;
    try {
      post = postsRepository.findPostById(id);
    } catch (NoSuchElementException n) {
      n.getMessage();
      return null;
    }

    return postMapper.toPostResponseById(post);
  }

  public PostResponse getMyPosts(Integer offset, Integer limit, String status) {
    PostResponse postResponse = new PostResponse();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    main.model.User currentUser = userRepository.findByEmail(user.getUsername());
    Sort sort = Sort.by("time").descending();

    if (status.equals("inactive")) {
      Page<Post> posts =
          postsRepository.findStatusInactiveByPosts(
              currentUser.getId(), getSortedPaging(offset, limit, sort));
      postResponse.setCount(posts.getTotalElements());
      settersPostsResponse(postResponse, posts);
    }

    if (status.equals("pending")) {
      Page<Post> posts =
          postsRepository.findStatusPendingByPosts(
              currentUser.getId(), getSortedPaging(offset, limit, sort));
      postResponse.setCount(posts.getTotalElements());
      settersPostsResponse(postResponse, posts);
    }

    if (status.equals("declined")) {
      Page<Post> posts =
          postsRepository.findStatusDeclinedByPosts(
              currentUser.getId(), getSortedPaging(offset, limit, sort));
      postResponse.setCount(posts.getTotalElements());
      settersPostsResponse(postResponse, posts);
    }

    if (status.equals("published")) {
      Page<Post> posts =
          postsRepository.findStatusPublishedByPosts(
              currentUser.getId(), getSortedPaging(offset, limit, sort));
      postResponse.setCount(posts.getTotalElements());
      settersPostsResponse(postResponse, posts);
    }

    return postResponse;
  }

  public PostResponse getModerationPost(Integer offset, Integer limit, String status) {
    PostResponse postResponse = new PostResponse();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    main.model.User currentUser = userRepository.findByEmail(user.getUsername());

    ModerationStatus moderationStatus = ModerationStatus.NEW;
    if (status.equals("accepted")) {
      moderationStatus = ModerationStatus.ACCEPTED;
    }

    if (status.equals("declined")) {
      moderationStatus = ModerationStatus.DECLINED;
    }

    Page<Post> posts =
        postsRepository.findModeratedPost(
            currentUser.getId(),
            moderationStatus,
            getSortedPaging(offset, limit, Sort.by("time").descending()));

    postResponse.setCount(posts.getTotalElements());
    settersPostsResponse(postResponse, posts);

    return postResponse;
  }

  public NewPostResponse addNewPost(
      @RequestBody NewPostRequest newPostRequest, Principal principal) {
    NewPostResponse newPostsResponse = new NewPostResponse();
    main.model.User currentUser = userRepository.findByEmail(principal.getName());
    ErrorNewPostDto errorNewPostDTO = new ErrorNewPostDto();

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
    /*TODO тут нужно разобраться:
    *  1. Со временем сохранения постов, отстают на 3 часа
       2. У поста, если пустой заголовок, он все равно сохраняет пост
       3. Попробовать сделать маппер на низ
       4. Дублирование строк update - add new. Методы 239, 286*/
    Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    time.setTimeInMillis(newPostRequest.getTimestamp() * 1000);
    post.setTime(time);
    post.setIsActive(newPostRequest.getActive().equals(true) ? 1 : 0);
    post.setModeratorId(currentUser.getIsModerator());
    post.setUser(currentUser);
    post.setTime(time);
    post.setTitle(title);
    post.setText(text);
    post.setViewCount(0);
    postsRepository.save(post);
    post.setTagList(lookTag(newPostRequest.getTags(), post));
    postsRepository.save(post);
    newPostsResponse.setErrors(errorNewPostDTO);
    return newPostsResponse;
  }

  public NewPostResponse updatePost(
      Integer id, NewPostRequest newPostRequest, Principal principal) {
    NewPostResponse newPostsResponse = new NewPostResponse();
    main.model.User currentUser = userRepository.findByEmail(principal.getName());
    ErrorNewPostDto errorNewPostDTO = new ErrorNewPostDto();

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
    if (requestTime > calendarTime) {
      time.setTimeInMillis(requestTime);
    } else {
      time.setTimeInMillis(calendarTime);
    }
    post.setTime(time);

    Boolean isAuthor = currentUser.getName().equals(post.getUser().getName());
    Boolean isModerator = currentUser.getIsModerator() == 1;
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

package main.service;

import main.dto.ErrorCreatePostDto;
import main.dto.api.request.CreatePostRequest;
import main.dto.api.response.CalendarResponse;
import main.dto.api.response.OperationPostResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static main.mappers.converter.DateConverter.dateToLong;
import static main.mappers.converter.DateConverter.longToDate;
import static main.mappers.converter.ResultValue.*;

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
      GlobalSettingRepository globalSettingRepository) {
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
              getSortedPaging(offset, limit, Sort.by(TIME).descending()));
    } else {
      posts = postsRepository.findPostsBySearch(getPaging(offset, limit), query);
    }
    settersPostsResponse(postResponse, posts);
    return postResponse;
  }

  public PostResponse getPosts(Integer offset, Integer limit, String mode) {
    PostResponse postResponse = new PostResponse();

    if (mode.equals(RECENT)) {
      Page<Post> posts =
          postsRepository.findAllPostsSortedByRecentOrEarly(
              getSortedPaging(offset, limit, Sort.by(TIME).descending()));
      settersPostsResponse(postResponse, posts);
    }

    if (mode.equals(EARLY)) {
      Page<Post> posts =
          postsRepository.findAllPostsSortedByRecentOrEarly(
              getSortedPaging(offset, limit, Sort.by(TIME).ascending()));
      settersPostsResponse(postResponse, posts);
    }

    if (mode.equals(POPULAR)) {
      Page<Post> posts = postsRepository.findAllPostOrderByComments(getPaging(offset, limit));
      settersPostsResponse(postResponse, posts);
    }

    if (mode.equals(BEST)) {
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
    Post post = postsRepository.findById(id).orElse(null);
    return postMapper.toPostResponseById(post);
  }

  public PostResponse getMyPosts(Integer offset, Integer limit, String status) {
    PostResponse postResponse = new PostResponse();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    main.model.User currentUser = userRepository.findByEmail(user.getUsername());
    Sort sort = Sort.by(TIME).descending();

    if (status.equals(INACTIVE)) {
      Page<Post> posts =
          postsRepository.findStatusInactiveByPosts(
              currentUser.getId(), getSortedPaging(offset, limit, sort));
      postResponse.setCount(posts.getTotalElements());
      settersPostsResponse(postResponse, posts);
    }

    if (status.equals(PENDING)) {
      Page<Post> posts =
          postsRepository.findStatusPendingByPosts(
              currentUser.getId(), getSortedPaging(offset, limit, sort));
      postResponse.setCount(posts.getTotalElements());
      settersPostsResponse(postResponse, posts);
    }

    if (status.equals(DECLINED)) {
      Page<Post> posts =
          postsRepository.findStatusDeclinedByPosts(
              currentUser.getId(), getSortedPaging(offset, limit, sort));
      postResponse.setCount(posts.getTotalElements());
      settersPostsResponse(postResponse, posts);
    }

    if (status.equals(PUBLISHED)) {
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
    if (status.equals(ACCEPTED)) {
      moderationStatus = ModerationStatus.ACCEPTED;
    }

    if (status.equals(DECLINED)) {
      moderationStatus = ModerationStatus.DECLINED;
    }

    Page<Post> posts =
        postsRepository.findModeratedPost(
            currentUser.getId(),
            moderationStatus,
            getSortedPaging(offset, limit, Sort.by(TIME).descending()));

    postResponse.setCount(posts.getTotalElements());
    settersPostsResponse(postResponse, posts);
    return postResponse;
  }

  public OperationPostResponse addNewPost(
      @RequestBody CreatePostRequest createPostRequest, Principal principal) {

    OperationPostResponse operationPostResponse = publicationCheck(createPostRequest);
    if (!operationPostResponse.getResult()) {
      return operationPostResponse;
    }
    main.model.User currentUser = userRepository.findByEmail(principal.getName());
    Post post = new Post();

    GlobalSetting postPremoderation = globalSettingRepository.findByCode(POST_PREMODERATION);
    if (postPremoderation.getValue().equals(YES)) {
      post.setModerationStatus(ModerationStatus.NEW);
    } else {
      post.setModerationStatus(ModerationStatus.ACCEPTED);
    }

    post = postMapper.toAddOrUpdatePost(post, createPostRequest, currentUser);
    postsRepository.save(post);
    post.setTagList(lookTag(createPostRequest.getTags(), post));
    postsRepository.save(post);
    return operationPostResponse;
  }

  public OperationPostResponse updatePost(
      Integer id, CreatePostRequest createPostRequest, Principal principal) {

    OperationPostResponse operationPostsResponse = publicationCheck(createPostRequest);
    if (!operationPostsResponse.getResult()) {
      return operationPostsResponse;
    }

    main.model.User currentUser = userRepository.findByEmail(principal.getName());
    Post post = postsRepository.findPostById(id);
    post = postMapper.toAddOrUpdatePost(post, createPostRequest, currentUser);
    post.setTagList(lookTag(createPostRequest.getTags(), post));

    Long date = dateToLong(LocalDateTime.now());
    if (createPostRequest.getTimestamp() < date) {
      post.setTime(longToDate(date));
    }
    if (currentUser.getName().equals(post.getUser().getName())) {
      post.setModerationStatus(ModerationStatus.NEW);
    }
    if (currentUser.getIsModerator() == ONE) {
      post.setModerationStatus(post.getModerationStatus());
    }
    postsRepository.save(post);
    return operationPostsResponse;
  }

  private OperationPostResponse publicationCheck(CreatePostRequest createPostRequest) {
    OperationPostResponse operationPostsResponse = new OperationPostResponse();
    ErrorCreatePostDto errorCreatePostDTO = new ErrorCreatePostDto();
    operationPostsResponse.setResult(false);
    if (createPostRequest.getTitle().length() < MIN_TITLE_LENGTH
        || createPostRequest.getTitle().isEmpty()) {
      errorCreatePostDTO.setTitle("Заголовок не установлен");
      operationPostsResponse.setErrors(errorCreatePostDTO);
      return operationPostsResponse;
    } else if (createPostRequest.getText().length() < MIN_TEXT_LENGTH
        || createPostRequest.getText().isEmpty()) {
      errorCreatePostDTO.setText("Текст публикации слишком короткий");
      operationPostsResponse.setErrors(errorCreatePostDTO);
      return operationPostsResponse;
    } else {
      operationPostsResponse.setErrors(errorCreatePostDTO);
      operationPostsResponse.setResult(true);
      return operationPostsResponse;
    }
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

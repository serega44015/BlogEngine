package main.controller;

import main.dto.api.request.LikeDislikeRequest;
import main.dto.api.request.NewPostRequest;
import main.dto.api.response.LikeDislikeResponse;
import main.dto.api.response.NewPostResponse;
import main.dto.api.response.PostIdResponse;
import main.dto.api.response.PostResponse;
import main.service.LikeDislikeService;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

  private final PostService postService;
  private final LikeDislikeService likeDislikeService;

  @Autowired
  public ApiPostController(PostService postService, LikeDislikeService likeDislikeService) {
    this.postService = postService;
    this.likeDislikeService = likeDislikeService;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<PostResponse> posts(
      @RequestParam(defaultValue = "0", required = false) Integer offset,
      @RequestParam(defaultValue = "10", required = false) Integer limit,
      @RequestParam(defaultValue = "popular", required = false) String mode) {
    return new ResponseEntity<>(postService.getPosts(offset, limit, mode), HttpStatus.OK);
  }

  @GetMapping("/search")
  @PreAuthorize("hasAuthority('user:moderate')")
  public ResponseEntity<PostResponse> postSearch(
      @RequestParam(defaultValue = "0", required = false) Integer offset,
      @RequestParam(defaultValue = "10", required = false) Integer limit,
      @RequestParam(defaultValue = "", required = false) String query) {
    return new ResponseEntity<>(postService.getPostBySearch(offset, limit, query), HttpStatus.OK);
  }

  @GetMapping("/byDate")
  public ResponseEntity<PostResponse> searchPostByDate(
      @RequestParam(defaultValue = "0", required = false) Integer offset,
      @RequestParam(defaultValue = "10", required = false) Integer limit,
      @RequestParam(defaultValue = "", required = false) String date) {
    return new ResponseEntity<>(postService.getPostByDate(offset, limit, date), HttpStatus.OK);
  }

  @GetMapping("/byTag")
  public ResponseEntity<PostResponse> searchPostByTag(
      @RequestParam(defaultValue = "0", required = false) Integer offset,
      @RequestParam(defaultValue = "10", required = false) Integer limit,
      @RequestParam(defaultValue = "", required = false) String tag) {
    return new ResponseEntity<>(postService.getPostByTag(offset, limit, tag), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostIdResponse> postById(@PathVariable("id") Integer id)
      throws NoSuchElementException {
    PostIdResponse postIdResponse = postService.getPostById(id);
    if (Objects.isNull(postIdResponse)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(postIdResponse, HttpStatus.OK);
  }

  @GetMapping("/my")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<PostResponse> myPost(
      @RequestParam(defaultValue = "0", required = false) Integer offset,
      @RequestParam(defaultValue = "10", required = false) Integer limit,
      @RequestParam(defaultValue = "status", required = false) String status) {

    return new ResponseEntity<>(postService.getMyPosts(offset, limit, status), HttpStatus.OK);
  }

  @GetMapping("/moderation")
  @PreAuthorize("hasAuthority('user:moderate')")
  public ResponseEntity<PostResponse> postModeration(
      @RequestParam(defaultValue = "0", required = false) Integer offset,
      @RequestParam(defaultValue = "10", required = false) Integer limit,
      @RequestParam(defaultValue = "status", required = false) String status) {

    return new ResponseEntity<>(
        postService.getModerationPost(offset, limit, status), HttpStatus.OK);
  }

  @PostMapping
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<NewPostResponse> newPost(
      @RequestBody NewPostRequest newPostRequest, Principal principal) {
    return new ResponseEntity<>(postService.addNewPost(newPostRequest, principal), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<NewPostResponse> updatePost(
      @PathVariable int id, @RequestBody NewPostRequest newPostRequest, Principal principal) {
    return new ResponseEntity<>(
        postService.updatePost(id, newPostRequest, principal), HttpStatus.OK);
  }

  @PostMapping("/like")
  @PreAuthorize("hasAuthority('user:write')")
  public LikeDislikeResponse likePost(
      @RequestBody LikeDislikeRequest likeRequest, Principal principal) {
    return likeDislikeService.getReactionPost(likeRequest, principal, true);
  }

  @PostMapping("/dislike")
  @PreAuthorize("hasAuthority('user:write')")
  public LikeDislikeResponse dislikePost(
      @RequestBody LikeDislikeRequest likeRequest, Principal principal) {
    return likeDislikeService.getReactionPost(likeRequest, principal, false);
  }
}

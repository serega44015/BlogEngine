package main.controller;

import main.dto.api.response.PostsResponse;
import main.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")

public class ApiPostController {

    private final PostsService postsService;

    @Autowired
    public ApiPostController(PostsService postsService) {
        this.postsService = postsService;
    }


    @GetMapping
    public ResponseEntity<PostsResponse> posts(
            @RequestParam(defaultValue = "0", required = false) Integer offset,
            @RequestParam(defaultValue = "10", required = false) Integer limit,
            @RequestParam(defaultValue = "popular", required = false) String mode) {
        return new ResponseEntity<>(postsService.getPosts(offset, limit, mode), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<PostsResponse> postSearch(
            @RequestParam(defaultValue = "0", required = false) Integer offset,
            @RequestParam(defaultValue = "10", required = false) Integer limit,
            @RequestParam(defaultValue = "", required = false) String query) {
        return new ResponseEntity<>(postsService.getPostBySearch(offset, limit, query), HttpStatus.OK);
    }

}

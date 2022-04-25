package main.controller;

import main.api.response.PostsResponse;
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

//    @GetMapping
//    private PostsResponse posts(
//            @RequestParam(value = "offset", defaultValue = "0") int offset,
//            @RequestParam(value = "limit", defaultValue = "10") int limit,
//            @RequestParam(value = "mode", defaultValue = "recent") String mode)
//    {
//        ResponseEntity
//        return postsService.getPosts(offset, limit, mode);
//    }

    //@GetMapping("/post")
    @GetMapping
    public ResponseEntity<PostsResponse> posts(
            @RequestParam(defaultValue = "0", required = false) Integer offset,
            @RequestParam(defaultValue = "10", required = false) Integer limit,
            @RequestParam(defaultValue = "recent", required = false) String mode) {
        //return new ResponseEntity<>(postsService.getPost(offset, limit, mode), HttpStatus.OK);
        return new ResponseEntity<>(postsService.getPosts(offset, limit, mode), HttpStatus.OK);
    }


}

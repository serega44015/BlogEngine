package main.controller;

import main.dto.api.response.PostsIdResponse;
import main.dto.api.response.PostsResponse;
import main.model.Post;
import main.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

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

    @GetMapping("/byDate")
    public ResponseEntity<PostsResponse> searchPostByDate(
            @RequestParam(defaultValue = "0", required = false) Integer offset,
            @RequestParam(defaultValue = "10", required = false) Integer limit,
            @RequestParam(defaultValue = "", required = false) String date) {
        return new ResponseEntity<>(postsService.getPostByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostsResponse> searchPostByTag(
            @RequestParam(defaultValue = "0", required = false) Integer offset,
            @RequestParam(defaultValue = "10", required = false) Integer limit,
            @RequestParam(defaultValue = "", required = false) String tag) {
        return new ResponseEntity<>(postsService.getPostByTag(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostsIdResponse> postById(@PathVariable("id") int id) throws NoSuchElementException {

//        if ("servicenull"){
//            System.out.println("return, http.ok else hpppt.huk");
//        }
        PostsIdResponse postsIdResponse = postsService.getPostById(id);
        if (postsIdResponse == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(postsIdResponse, HttpStatus.OK);
    }


}

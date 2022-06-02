package main.controller;

import main.dto.api.request.CommentRequest;
import main.dto.api.request.SettingsRequest;
import main.dto.api.response.*;
import main.service.CommentService;
import main.service.PostsService;
import main.service.SettingService;
import main.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final SettingService settingService;
    private final InitResponse initResponse;
    private final TagService tagService;
    private final PostsService postsService;
    private final CommentService commentService;

    @Autowired
    public ApiGeneralController(SettingService settingService, InitResponse initResponse, TagService tagService, PostsService postsService, CommentService commentService) {
        this.settingService = settingService;
        this.initResponse = initResponse;
        this.tagService = tagService;
        this.postsService = postsService;
        this.commentService = commentService;
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return settingService.getSetting();
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAuthority('user:moderate')")
    public void changeSettings(@RequestBody SettingsRequest settingsRequest) {
        settingService.putSetting(settingsRequest);
    }

    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/tag")
    public TagsResponse tag(@PathVariable @Nullable String query) {
        return tagService.getTags();
    }


    @GetMapping("calendar")
    private CalendarResponse calendar(
            @RequestParam(value = "year", defaultValue = "") String year) {

        return postsService.getPostByYear(year);
    }

    @PostMapping("/comment")
    @PreAuthorize("hasAuthority('user:write')")
    public CommentResponse comment(@RequestBody CommentRequest commentRequest, Principal principal) {
        System.out.println("Заходим?");
        return commentService.postComment(commentRequest, principal);
    }





}

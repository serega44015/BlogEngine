package main.controller;

import main.dto.api.response.CalendarResponse;
import main.dto.api.response.InitResponse;
import main.dto.api.response.SettingsResponse;
import main.dto.api.response.TagsResponse;
import main.service.PostsService;
import main.service.SettingsService;
import main.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final SettingsService settingsService;
    private final InitResponse initResponse;
    private final TagsService tagsService;
    private final PostsService postsService;

    @Autowired
    public ApiGeneralController(SettingsService settingsService, InitResponse initResponse, TagsService tagsService, PostsService postsService) {
        this.settingsService = settingsService;
        this.initResponse = initResponse;
        this.tagsService = tagsService;
        this.postsService = postsService;
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return settingsService.getInitGlobalSettings();
    }

    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/tag")
    private TagsResponse tags(
            @RequestParam(value = "query", defaultValue = "") String tagName) {
        return tagsService.getTags(tagName);
    }

    @GetMapping("calendar")
    private CalendarResponse calendar(
            @RequestParam(value = "year", defaultValue = "") String year) {

        return postsService.getPostByYear(year);
    }




}

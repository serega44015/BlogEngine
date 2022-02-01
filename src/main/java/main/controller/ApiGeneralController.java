package main.controller;

import main.dto.api.response.InitResponse;
import main.dto.api.response.SettingsResponse;
import main.dto.api.response.TagsResponse;
import main.service.SettingsService;
import main.service.TagsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final SettingsService settingsService;
    private final InitResponse initResponse;
    private final TagsService tagsService;

    public ApiGeneralController(SettingsService settingsService, InitResponse initResponse, TagsService tagsService) {
        this.settingsService = settingsService;
        this.initResponse = initResponse;
        this.tagsService = tagsService;
    }

    @GetMapping("/settings")
    private SettingsResponse settings(){
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
}

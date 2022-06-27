package main.controller;

import main.dto.api.request.CommentRequest;
import main.dto.api.request.ModerationRequest;
import main.dto.api.request.ProfileRequest;
import main.dto.api.request.SettingsRequest;
import main.dto.api.response.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

  private final SettingService settingService;
  private final InitResponse initResponse;
  private final TagService tagService;
  private final PostService postService;
  private final CommentService commentService;
  private final ModerateService moderateService;
  private final ProfileService profileService;
  private final StatsService statsService;

  @Autowired
  public ApiGeneralController(
      SettingService settingService,
      InitResponse initResponse,
      TagService tagService,
      PostService postService,
      CommentService commentService,
      ModerateService moderateService,
      ProfileService profileService,
      StatsService statsService) {
    this.settingService = settingService;
    this.initResponse = initResponse;
    this.tagService = tagService;
    this.postService = postService;
    this.commentService = commentService;
    this.moderateService = moderateService;
    this.profileService = profileService;
    this.statsService = statsService;
  }

  @GetMapping("/settings")
  public GlobalSettingsResponse settings() {
    return settingService.getSettings();
  }

  @PutMapping("/settings")
  @PreAuthorize("hasAuthority('user:moderate')")
  public void changeSettings(@RequestBody SettingsRequest settingsRequest) {
    settingService.putSetting(settingsRequest);
  }

  @GetMapping("/init")
  public InitResponse init() {
    return initResponse;
  }

  @GetMapping("/tag")
  public TagResponse tag() {
    return tagService.getTags();
  }

  @GetMapping("/calendar")
  public CalendarResponse calendar() {
    return postService.getPostsByYear();
  }

  @PostMapping("/comment")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<CommentResponse> comment(
      @RequestBody CommentRequest commentRequest, Principal principal) {
    return commentService.postComment(commentRequest, principal);
  }

  @PostMapping("/moderation")
  @PreAuthorize("hasAuthority('user:moderate')")
  public ModerationResponse moderatePost(
      @RequestBody ModerationRequest moderationRequest, Principal principal) {
    return moderateService.getModerator(moderationRequest, principal);
  }

  @PostMapping(
      value = "/profile/my",
      consumes = {"multipart/form-data"})
  @PreAuthorize("hasAuthority('user:write')")
  public ProfileResponse editProfile(
      @RequestPart("photo") byte[] photo,
      @RequestParam(value = "name") String name,
      @RequestParam(value = "email") String email,
      @RequestParam(value = "password", required = false) String password,
      @RequestParam(value = "removePhoto", defaultValue = "0") Integer removePhoto,
      Principal principal,
      HttpServletRequest request) {
    return profileService.getEditProfile(
        photo, name, email, password, removePhoto, principal, request);
  }

  @PostMapping(
      value = "/profile/my",
      consumes = {"application/json"})
  @PreAuthorize("hasAuthority('user:write')")
  public ProfileResponse editProfileJson(
      @RequestBody ProfileRequest profileRequest, Principal principal, HttpServletRequest request) {
    return profileService.getJsonEditProfile(profileRequest, principal, request);
  }

  @GetMapping("/statistics/my")
  @PreAuthorize("hasAuthority('user:write')")
  public StatisticResponse myStats(Principal principal) {
    return statsService.getMyStatistics(principal);
  }

  @GetMapping("/statistics/all")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<StatisticResponse> allStats(Principal principal) {
    return statsService.getAllStatistics(principal);
  }
}

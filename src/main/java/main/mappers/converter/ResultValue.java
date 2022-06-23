package main.mappers.converter;

import main.model.PostComment;
import main.model.PostVote;
import main.model.Tag;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResultValue {
  public static final Integer ZERO = 0;
  public static final Integer ONE = 1;
  public static final String TIME = "time";
  public static final String RECENT = "recent";
  public static final String EARLY = "early";
  public static final String POPULAR = "popular";
  public static final String BEST = "best";
  public static final String INACTIVE = "inactive";
  public static final String PENDING = "pending";
  public static final String DECLINED = "declined";
  public static final String PUBLISHED = "published";
  public static final String ACCEPTED = "accepted";
  public static final String POST_PREMODERATION = "POST_PREMODERATION";
  public static final String YES = "YES";
  public static final String NO = "NO";

  @Named("tagNameList")
  public List<String> tagNameList(List<Tag> tagList) {
    return tagList.stream().map(t -> t.getName()).collect(Collectors.toList());
  }

  @Named("postIntIsActive")
  public Integer postIntIsActive(Boolean status) {
    return status ? ONE : ZERO;
  }

  @Named("postBoolIsActive")
  public Boolean postBoolIsActive(Integer isActive) {
    return isActive == ONE ? true : false;
  }

  @Named("likesAmount")
  public int likesAmount(List<PostVote> postVoteList) {
    return postVoteList.stream().filter(p -> p.getValue() == 1).collect(Collectors.toList()).size();
  }

  @Named("dislikesAmount")
  public Integer dislikesAmount(List<PostVote> postVoteList) {
    return postVoteList.stream()
        .filter(p -> p.getValue() == -1)
        .collect(Collectors.toList())
        .size();
  }

  @Named("commentCount")
  public Integer commentCount(List<PostComment> postCommentList) {
    return postCommentList.size();
  }
}

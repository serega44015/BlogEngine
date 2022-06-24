package main.mappers.converter;

import main.model.PostComment;
import main.model.PostVote;
import main.model.Tag;
import org.apache.commons.io.FileUtils;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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
  public static final String ACCEPTED = "accept";
  public static final String NEW = "new";
  public static final String POST_PREMODERATION = "POST_PREMODERATION";
  public static final String STATISTICS_IS_PUBLIC = "STATISTICS_IS_PUBLIC";
  public static final String YES = "YES";
  public static final String NO = "NO";
  public static Boolean loadingResultPhoto = true;
  public static final String UPLOAD = "upload/";

  public static void downloadPhoto(HttpServletRequest request) {
    String copyTo = request.getServletContext().getRealPath("");
    File uploadDirectory = new File(UPLOAD);
    try {
      if (FileUtils.isDirectory(uploadDirectory)) {
        FileUtils.copyDirectoryToDirectory(uploadDirectory, new File(copyTo));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    loadingResultPhoto = false;
  }

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
    return postVoteList.stream()
        .filter(p -> p.getValue() == ONE)
        .collect(Collectors.toList())
        .size();
  }

  @Named("dislikesAmount")
  public Integer dislikesAmount(List<PostVote> postVoteList) {
    return postVoteList.stream()
        .filter(p -> p.getValue() == -ONE)
        .collect(Collectors.toList())
        .size();
  }

  @Named("commentCount")
  public Integer commentCount(List<PostComment> postCommentList) {
    return postCommentList.size();
  }
}

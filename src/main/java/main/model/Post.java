package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.enums.ModerationStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT")
  private Integer isActive;

  @Column(name = "moderation_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private ModerationStatus moderationStatus = ModerationStatus.NEW;

  @Column(name = "moderator_id", columnDefinition = "INT")
  private Integer moderatorId;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, columnDefinition = "DATETIME")
  private LocalDateTime time;

  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String text;

  @Column(name = "view_count", columnDefinition = "INT")
  private Integer viewCount;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<PostVote> postVoteList;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<PostComment> postCommentList;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "tags2post",
      joinColumns = {@JoinColumn(name = "post_id")},
      inverseJoinColumns = {@JoinColumn(name = "tag_id")})
  private List<Tag> tagList;
}

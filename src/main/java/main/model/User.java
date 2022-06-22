package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.enums.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(name = "is_moderator", nullable = false, columnDefinition = "TINYINT")
  private Integer isModerator;

  @Column(name = "reg_time", nullable = false, columnDefinition = "DATETIME")
  private LocalDateTime regTime;

  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private String name;

  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private String email;

  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private String password;

  @Column(nullable = true, columnDefinition = "VARCHAR(255)")
  private String code;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String photo;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Post> userPosts;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<PostComment> postCommentUser;

  public Role getRole() {
    return isModerator == 1 ? Role.MODERATOR : Role.USER;
  }
}

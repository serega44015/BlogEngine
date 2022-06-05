package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private String name;

  @ManyToMany(mappedBy = "tagList")
  private List<Post> postWithTags;
}

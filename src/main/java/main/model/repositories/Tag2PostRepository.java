package main.model.repositories;

import main.model.Tag2Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Tag2PostRepository extends JpaRepository<Tag2Post, Integer> {

  @Query(
      value =
          "SELECT COUNT(tgPost.postId) FROM Tag2Post tgPost INNER JOIN Post p ON p.id = tgPost.postId INNER JOIN  Tag tg ON tg.id = tgPost.tagId WHERE tg.name = :tagName ")
  Integer countOfPostsWithTheName(@Param("tagName") String tagName);
}

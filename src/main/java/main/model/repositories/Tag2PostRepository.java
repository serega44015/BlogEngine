package main.model.repositories;

import main.model.Tags2Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface Tag2PostRepository extends JpaRepository<Tags2Post, Integer> {

    @Query(value = "SELECT COUNT(tgPost.postId) FROM Tags2Post tgPost INNER JOIN Post p ON p.id = tgPost.postId INNER JOIN  Tag tg ON tg.id = tgPost.tagId WHERE tg.name = :tagName ")
    Integer countOfPostsWithTheName(@Param("tagName") String tagName);




}

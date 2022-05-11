package main.model.repositories;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Override
    List<Tag> findAll();

    @Query(value = "SELECT tg.id FROM Tag tg WHERE tg.name = :tagName")
    Integer findTagIdByName(@Param("tagName") String tagName);

//    SELECT tags.id FROM tags
//    WHERE tags.name = 'море'
}

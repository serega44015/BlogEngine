package main.model.repositories;

import main.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Integer> {

    @Query(value = "SELECT p FROM Posts p " +
            "LEFT JOIN User u ON p.userId.id = u.id " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= current_time")
    Page<Posts> findAllPostsSortedByRecent(Pageable pageable);

    @Query(value = "SELECT p FROM Posts p " +
            "LEFT JOIN User u ON p.userId.id = u.id " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= current_time")
    Page<Posts> findAllPostsSortedByEarly(Pageable pageable);


    @Query(value = "SELECT p FROM Posts p " +
            "LEFT JOIN User u ON p.userId.id = u.id " +
            "LEFT JOIN PostVotes pv1 ON p.id = pv1.postsId.id AND pv1.value = 1 " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pv1.value) DESC")
    Page<Posts> findAllPostOrderByLikes(Pageable pageable); //AND p.time <= current_time проблемма с проверкой по времени, из-за этого не сортирует по лайкам



}

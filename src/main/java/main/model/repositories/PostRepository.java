package main.model.repositories;

import main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Integer> {


    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN User u ON p.userId.id = u.id " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= CURRENT_TIME")
    Page<Post> findAllPostsSortedByRecent(Pageable pageable);

    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN User u ON p.userId.id = u.id " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= CURRENT_TIME")
    Page<Post> findAllPostsSortedByEarly(Pageable pageable);


    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN User u ON p.userId.id = u.id " +
            "LEFT JOIN PostVotes pv1 ON  pv1.postId.id = p.id AND pv1.value = 1 " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= CURRENT_TIME " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pv1.value) DESC")
    Page<Post> findAllPostOrderByLikes(Pageable pageable);

    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN User u ON p.userId.id = u.id " +
            "LEFT JOIN PostComments pc1 ON pc1.postId.id = p.id " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pc1.id) DESC")
    Page<Post> findAllPostOrderByComments(Pageable pageable);

}

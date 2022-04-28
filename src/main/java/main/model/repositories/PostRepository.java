package main.model.repositories;

import lombok.NonNull;
import main.model.Post;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;

import javax.validation.constraints.NotNull;

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


    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN User u ON p.userId.id = u.id " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= CURRENT_TIME " +
            "AND p.text LIKE %:query%")
    Page<Post> findPostsBySearch(Pageable pageable, @Param("query") String query);

//    SELECT count(posts.id) FROM posts
//    INNER JOIN post_votes ON posts.id = post_votes.post_id
//    WHERE post_votes.value = '1' and posts.id = '1'
//    group by posts.id;


    @Query(value = "SELECT COUNT(p.id) FROM Post p " +
            "INNER JOIN PostVotes pv ON p.id = pv.postId.id " +
            "WHERE pv.value = 1 AND p.id = :postsId " +
            "GROUP BY p.id")
    Integer countOfLikesPerPost(@Param("postsId") Integer postsId);

    @Query(value = "SELECT COUNT(p.id) FROM Post p " +
            "INNER JOIN PostVotes pv ON p.id = pv.postId.id " +
            "WHERE pv.value = -1 AND p.id = :postsId " +
            "GROUP BY p.id")
    Integer countOfDisLikesPerPost(@Param("postsId") Integer postsId);

    @Query(value = "SELECT COUNT(p.id) FROM Post p INNER JOIN PostComments pc ON p.id = pc.postId.id WHERE p.id = :postsId GROUP BY p.id")
    Integer countOfCommentsPerComments(@Param("postsId") Integer postsId);

//    SELECT count(posts.id) FROM posts
//    INNER JOIN posts_comments ON posts.id = posts_comments.post_id
//    WHERE post_id = 3
//    group by posts_comments.post_id

    @Query(value = "SELECT COUNT(p.id) FROM Post p")
    Integer countPosts();

}

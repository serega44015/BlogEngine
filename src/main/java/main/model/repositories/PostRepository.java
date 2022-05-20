package main.model.repositories;

import main.model.Post;
import main.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public interface PostRepository extends JpaRepository<Post, Integer> {


    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN User u ON p.user.id = u.id " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= CURRENT_TIME")
    Page<Post> findAllPostsSortedByRecent(Pageable pageable);

    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN User u ON p.user.id = u.id " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= CURRENT_TIME")
    Page<Post> findAllPostsSortedByEarly(Pageable pageable);


    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN User u ON p.user.id = u.id " +
            "LEFT JOIN PostVotes pv1 ON  pv1.postId.id = p.id AND pv1.value = 1 " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= CURRENT_TIME " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pv1.value) DESC")
    Page<Post> findAllPostOrderByLikes(Pageable pageable);

    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN User u ON p.user.id = u.id " +
            "LEFT JOIN PostComments pc1 ON pc1.post.id = p.id " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pc1.id) DESC")
    Page<Post> findAllPostOrderByComments(Pageable pageable);


    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN User u ON p.user.id = u.id " +
            "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= CURRENT_TIME " +
            "AND p.text LIKE %:query%")
    Page<Post> findPostsBySearch(Pageable pageable, @Param("query") String query);


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

    @Query(value = "SELECT COUNT(p.id) FROM Post p INNER JOIN PostComments pc ON p.id = pc.post.id WHERE p.id = :postsId GROUP BY p.id")
    Integer countOfCommentsPerComments(@Param("postsId") Integer postsId);

    @Query(value = "SELECT COUNT(p.id) FROM Post p")
    Integer countPosts();

    @Query(value = "SELECT YEAR(time) AS year FROM Post p")
    TreeSet<Integer> getSetYearsByAllPosts();


    @Query(value = "SELECT DATE(posts.time) FROM posts", nativeQuery = true)
    List<String> getDateFromPosts();


    @Query(value = "SELECT count(posts.time) FROM site.posts WHERE posts.time LIKE :date", nativeQuery = true)
    Integer countPostsFromDate(@Param("date") String date);


    @Query(value = "SELECT * FROM site.posts LEFT JOIN site.users ON posts.user_id = users.id " +
            "WHERE posts.moderation_status = 'ACCEPTED' and posts.is_active = 1 " +
            "and posts.time <= current_time() and DATE(site.posts.time) = :date", nativeQuery = true)
    Page<Post> findPostsByDate(Pageable pageable, @Param("date") String date);

    @Query(value = "SELECT p FROM Post p " +
            "INNER JOIN Tag2Post tgp2 ON tgp2.postId = p.id " +
            "WHERE p.moderationStatus = 'ACCEPTED' " +
            "AND p.isActive = 1 " +
            "AND p.time <= CURRENT_TIME " +
            "AND tgp2.tagId = :tagId")
    Page<Post> findPostsByTagId(Pageable pageable, @Param("tagId") Integer tagId);

    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "LEFT JOIN PostVotes pv1 ON pv1.postId.id = p.id AND pv1.value = 1 " +
            "LEFT JOIN PostVotes pv2 ON pv2.postId.id = p.id AND pv2.value = -1 " +
            "LEFT JOIN PostComments pc ON pc.post.id = p.id " +
            "LEFT JOIN Tag2Post t2p ON t2p.postId = p.id " +
            "LEFT JOIN Tag tg ON tg.id = t2p.tagId " +
            "WHERE p.id = :id AND p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= CURRENT_TIME " +
            "GROUP BY p.id")
    Optional<Post> findPostsById(@Param("id") Integer id);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "LEFT JOIN PostVotes pv1 ON pv1.postId.id = p.id AND pv1.value = 1 " +
            "LEFT JOIN PostVotes pv2 ON pv2.postId.id = p.id AND pv2.value = -1 " +
            "LEFT JOIN PostComments pc ON pc.post.id = p.id " +
            "WHERE p.isActive = 0 AND p.user.id = :userId " +
            "GROUP BY p.id")
    Page<Post> findStatusInactiveByPosts(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "LEFT JOIN PostVotes pv1 ON pv1.postId.id = p.id AND pv1.value = 1 " +
            "LEFT JOIN PostVotes pv2 ON pv2.postId.id = p.id AND pv2.value = -1 " +
            "LEFT JOIN PostComments pc ON pc.post.id = p.id " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'NEW' AND p.user.id = :userId " +
            "GROUP BY p.id")
    Page<Post> findStatusPendingByPosts(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "LEFT JOIN PostVotes pv1 ON pv1.postId.id = p.id AND pv1.value = 1 " +
            "LEFT JOIN PostVotes pv2 ON pv2.postId.id = p.id AND pv2.value = -1 " +
            "LEFT JOIN PostComments pc ON pc.post.id = p.id " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'DECLINED' AND p.user.id = :userId " +
            "GROUP BY p.id")
    Page<Post> findStatusDeclinedByPosts(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "LEFT JOIN PostVotes pv1 ON pv1.postId.id = p.id AND pv1.value = 1 " +
            "LEFT JOIN PostVotes pv2 ON pv2.postId.id = p.id AND pv2.value = -1 " +
            "LEFT JOIN PostComments pc ON pc.post.id = p.id " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.user.id = :userId " +
            "GROUP BY p.id")
    Page<Post> findStatusPublishedByPosts(@Param("userId") Integer userId, Pageable pageable);

}

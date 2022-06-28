package main.model.repositories;

import main.model.Post;
import main.model.User;
import main.model.enums.ModerationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public interface PostRepository extends JpaRepository<Post, Integer> {

  @Query(
      value =
          "SELECT p FROM Post p "
              + "LEFT JOIN User u ON p.user.id = u.id "
              + "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= CURRENT_TIME")
  Page<Post> findAllPostsSortedByRecentOrEarly(Pageable pageable);

  @Query(
      value =
          "SELECT p FROM Post p "
              + "LEFT JOIN User u ON p.user.id = u.id "
              + "LEFT JOIN PostVote pv1 ON  pv1.post.id = p.id AND pv1.value = 1 "
              + "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= CURRENT_TIME "
              + "GROUP BY p.id "
              + "ORDER BY COUNT(pv1.value) DESC")
  Page<Post> findAllPostOrderByLikes(Pageable pageable);

  @Query(
      value =
          "SELECT p FROM Post p "
              + "LEFT JOIN User u ON p.user.id = u.id "
              + "LEFT JOIN PostComment pc1 ON pc1.post.id = p.id "
              + "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 "
              + "GROUP BY p.id "
              + "ORDER BY COUNT(pc1.id) DESC")
  Page<Post> findAllPostOrderByComments(Pageable pageable);

  @Query(
      value =
          "SELECT p FROM Post p "
              + "LEFT JOIN User u ON p.user.id = u.id "
              + "WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 AND p.time <= CURRENT_TIME "
              + "AND p.text LIKE %:query%")
  Page<Post> findPostsBySearch(Pageable pageable, @Param("query") String query);

  @Query(value = "SELECT COUNT(p.id) FROM Post p")
  Integer countPosts();

  @Query(value = "SELECT YEAR(time) AS year FROM Post p")
  TreeSet<Integer> getSetYearsByAllPosts();

  @Query(value = "SELECT DATE(posts.time) FROM posts", nativeQuery = true)
  List<String> getDateFromPosts();

  @Query(
      value =
          "SELECT count(time) FROM posts WHERE time LIKE :date AND moderation_status = 'ACCEPTED'",
      nativeQuery = true)
  Integer countPostsFromDate(@Param("date") String date);

  @Query(
      value =
          "SELECT * FROM posts LEFT JOIN users ON posts.user_id = users.id "
              + "WHERE posts.moderation_status = 'ACCEPTED' and posts.is_active = 1 "
              + "and posts.time <= current_time() and posts.time LIKE :date",
      nativeQuery = true)
  Page<Post> findPostsByDate(Pageable pageable, @Param("date") String date);

  @Query(
      value =
          "SELECT p FROM Post p "
              + "INNER JOIN Tag2Post tgp2 ON tgp2.postId = p.id "
              + "WHERE p.moderationStatus = 'ACCEPTED' "
              + "AND p.isActive = 1 "
              + "AND p.time <= CURRENT_TIME "
              + "AND tgp2.tagId = :tagId")
  Page<Post> findPostsByTagId(Pageable pageable, @Param("tagId") Integer tagId);

  Post findPostById(Integer id);

  @Query(
      "SELECT p FROM Post p "
          + "LEFT JOIN User u ON u.id = p.user.id "
          + "LEFT JOIN PostVote pv1 ON pv1.post.id = p.id AND pv1.value = 1 "
          + "LEFT JOIN PostVote pv2 ON pv2.post.id = p.id AND pv2.value = -1 "
          + "LEFT JOIN PostComment pc ON pc.post.id = p.id "
          + "WHERE p.isActive = 0 AND p.user.id = :userId "
          + "GROUP BY p.id")
  Page<Post> findStatusInactiveByPosts(@Param("userId") Integer userId, Pageable pageable);

  @Query(
      "SELECT p FROM Post p "
          + "LEFT JOIN User u ON u.id = p.user.id "
          + "LEFT JOIN PostVote pv1 ON pv1.post.id = p.id AND pv1.value = 1 "
          + "LEFT JOIN PostVote pv2 ON pv2.post.id = p.id AND pv2.value = -1 "
          + "LEFT JOIN PostComment pc ON pc.post.id = p.id "
          + "WHERE p.isActive = 1 AND p.moderationStatus = 'NEW' AND p.user.id = :userId "
          + "GROUP BY p.id")
  Page<Post> findStatusPendingByPosts(@Param("userId") Integer userId, Pageable pageable);

  @Query(
      "SELECT p FROM Post p "
          + "LEFT JOIN User u ON u.id = p.user.id "
          + "LEFT JOIN PostVote pv1 ON pv1.post.id = p.id AND pv1.value = 1 "
          + "LEFT JOIN PostVote pv2 ON pv2.post.id = p.id AND pv2.value = -1 "
          + "LEFT JOIN PostComment pc ON pc.post.id = p.id "
          + "WHERE p.isActive = 1 AND p.moderationStatus = 'DECLINED' AND p.user.id = :userId "
          + "GROUP BY p.id")
  Page<Post> findStatusDeclinedByPosts(@Param("userId") Integer userId, Pageable pageable);

  @Query(
      "SELECT p FROM Post p "
          + "LEFT JOIN User u ON u.id = p.user.id "
          + "LEFT JOIN PostVote pv1 ON pv1.post.id = p.id AND pv1.value = 1 "
          + "LEFT JOIN PostVote pv2 ON pv2.post.id = p.id AND pv2.value = -1 "
          + "LEFT JOIN PostComment pc ON pc.post.id = p.id "
          + "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.user.id = :userId "
          + "GROUP BY p.id")
  Page<Post> findStatusPublishedByPosts(@Param("userId") Integer userId, Pageable pageable);

  @Query(
      "SELECT p FROM Post p "
          + "LEFT JOIN User u ON u.id = p.user.id "
          + "LEFT JOIN PostVote pv1 ON pv1.post.id = p.id AND pv1.value = 1 "
          + "LEFT JOIN PostVote pv2 ON pv2.post.id = p.id AND pv2.value = -1 "
          + "LEFT JOIN PostComment pc ON pc.post.id = p.id "
          + "WHERE p.isActive = 1 AND p.moderationStatus = :status AND p.moderatorId = :id "
          + "GROUP BY p.id")
  Page<Post> findModeratedPost(
      @Param("id") int id, @Param("status") ModerationStatus status, Pageable pageable);
}

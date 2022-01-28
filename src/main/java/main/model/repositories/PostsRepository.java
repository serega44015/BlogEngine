package main.model.repositories;

import main.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Integer> {


    Optional<Posts> findAllById(int id);

    //Page<Posts> findAll(Pageable pageable);
   // Page<Posts> findAllByUserId(Pageable pageable);

//    @Query(value = "SELECT * FROM posts " +
//            "LEFT JOIN users ON users.id = posts.user_id" +
//            "WHERE posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.time <= CURRENT_TIME() GROUP BY posts.id",
//            nativeQuery = true)
    Page<Posts> findAll(Pageable pageable);

    //Page<Posts> findPostsOrderByLikes(Pageable pageable); тут наверное нужно Query искать как пишется
    //findPostsOrderByLikes
    /*
    * SELECT * FROM posts
    LEFT JOIN users ON users.id = posts.user_id
    WHERE posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.time <= current_time()
    GROUP BY posts.id;
* */
}

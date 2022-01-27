package main.model.repositories;

import main.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Integer> {


    Optional<Posts> findAllById(int id);

    //Page<Posts> findAll(Pageable pageable);
   // Page<Posts> findAllByUserId(Pageable pageable);
    Page<Posts> findAll(Pageable pageable);

    //Page<Posts> findPostsOrderByLikes(Pageable pageable); тут наверное нужно Query искать как пишется
    //findPostsOrderByLikes

}

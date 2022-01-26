package main.model.repositories;

import main.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Integer> {


    Optional<Posts> findAllById(int id);
}

package main.model.repositories;

import main.model.Tags2Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Tag2PostRepository extends JpaRepository<Tags2Post, Integer> {
}

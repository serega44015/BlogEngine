package main.model.repositories;

import main.model.PostComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentsRepository extends JpaRepository<PostComments, Integer> {

}

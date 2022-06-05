package main.model.repositories;

import main.model.Post;
import main.model.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {

  PostVotes findByPostAndUserId(Post post, Integer userId);

  @Override
  void delete(PostVotes postVotes);

  @Override
  void deleteById(Integer integer);
}

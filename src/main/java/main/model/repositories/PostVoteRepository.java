package main.model.repositories;

import main.model.Post;
import main.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {

  PostVote findByPostAndUserId(Post post, Integer userId);

  @Override
  void delete(PostVote postVote);

  @Override
  void deleteById(Integer integer);
}

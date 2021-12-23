package main.model.repositories;

import main.model.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {

}

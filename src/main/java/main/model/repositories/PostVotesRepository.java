package main.model.repositories;

import main.model.PostVotes;
import main.model.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;

public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {



}

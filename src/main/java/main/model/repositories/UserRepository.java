package main.model.repositories;

import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    //TODO переделать везде на hibernate
    Optional<User> findByEmail(String email);

    Optional<User> findUserById(int id);

    User findByCode(String code);
}

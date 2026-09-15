package com.security.lab.repository;

import com.security.lab.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUsersByLogin(String login);
}

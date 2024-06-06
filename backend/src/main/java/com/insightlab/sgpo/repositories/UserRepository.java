package com.insightlab.sgpo.repositories;

import com.insightlab.sgpo.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.username= ?1")
    User findByUsername(String username);

    Boolean existsByUsername(String username);

}

package com.lotarys.devlink.repositories;


import com.lotarys.devlink.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.cards WHERE u.id = :userId")
    User findUserWithCards(@Param("userId") Long userId);
    Optional<User> findByEmail(String email);
}

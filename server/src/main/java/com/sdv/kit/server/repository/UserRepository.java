package com.sdv.kit.server.repository;

import com.sdv.kit.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "update User u " +
            "set u.isLoggedIn = false " +
            "where u.username = :username")
    void logoutUser(String username);

    @Modifying
    @Transactional
    @Query(value = "update User u " +
            "set u.isLoggedIn = true " +
            "where u.username = :username")
    void loginUser(String username);
}
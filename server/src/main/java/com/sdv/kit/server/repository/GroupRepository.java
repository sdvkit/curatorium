package com.sdv.kit.server.repository;

import com.sdv.kit.server.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(value = "select g from Group g join fetch g.students where g.user.username = :username")
    List<Group> findAllByUser(String username);
}
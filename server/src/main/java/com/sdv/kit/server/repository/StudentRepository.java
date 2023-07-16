package com.sdv.kit.server.repository;

import com.sdv.kit.server.model.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @EntityGraph(attributePaths = {"group"})
    @Query(value = "select s from Student s " +
            "join s.group.user u " +
            "where u.username = :username and s.id = :id")
    Optional<Student> findByIdAndUser(Long id, String username);
}
package com.sdv.kit.server.repository;

import com.sdv.kit.server.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query(value =
            "select s from Subject s " +
            "join s.user u " +
            "where u.username = :username and s.name = :subjectName")
    Optional<Subject> findExistsSubject(String subjectName, String username);

    @Query(value =
            "select s from Subject s " +
            "join s.user u " +
            "where u.username = :username")
    List<Subject> findAllByUser(String username);
}
package com.sdv.kit.server.repository;

import com.sdv.kit.server.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Modifying
    @Transactional
    @Query(value = "delete from students " +
            "using users " +
            "where students.id = :id and users.username = :username", nativeQuery = true)
    void deleteByIdAndUser(Long id, String username);

    @Modifying
    @Transactional
    @Query(value = "update students " +
            "set name = :newStudentName " +
            "from users " +
            "where users.username = :username and students.id = :id", nativeQuery = true)
    void renameStudent(Long id, String newStudentName, String username);
}
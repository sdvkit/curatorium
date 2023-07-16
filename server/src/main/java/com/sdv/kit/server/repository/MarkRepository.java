package com.sdv.kit.server.repository;

import com.sdv.kit.server.model.Mark;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    @EntityGraph(attributePaths = {"secondLvlStatement.firstLvlStatement.group.user"})
    @Query(value = "select m from Mark m " +
            "where m.secondLvlStatement.firstLvlStatement.group.user.username = :username and m.id = :id")
    Optional<Mark> findByIdAndUser(Long id, String username);
}
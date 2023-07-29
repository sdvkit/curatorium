package com.sdv.kit.server.repository;

import com.sdv.kit.server.model.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    @Modifying
    @Transactional
    @Query(value = "update marks " +
            "set value = :newValue " +
            "from users " +
            "where users.username = :username and marks.id = :id", nativeQuery = true)
    void editMark(Long id, Integer newValue, String username);
}
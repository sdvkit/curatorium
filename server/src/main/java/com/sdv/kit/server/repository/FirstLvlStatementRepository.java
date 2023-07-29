package com.sdv.kit.server.repository;

import com.sdv.kit.server.model.FirstLvlStatement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FirstLvlStatementRepository extends JpaRepository<FirstLvlStatement, Long> {

    @EntityGraph(attributePaths = {"group", "group.user"})
    @Query(value = "select fls from FirstLvlStatement fls " +
            "join fls.group.user u " +
            "where u.username = :username and fls.name = :flsName")
    Optional<FirstLvlStatement> findByNameAndUser(String flsName, String username);

    @EntityGraph(attributePaths = {"group", "group.user"})
    @Query(value = "select fls from FirstLvlStatement fls " +
            "join fls.group.user u " +
            "where u.username = :username and fls.id = :id")
    Optional<FirstLvlStatement> findByIdAndUser(Long id, String username);

    @EntityGraph(attributePaths = {"group", "group.user", "secondLvlStatements"})
    @Query(value = "select fls from FirstLvlStatement fls " +
            "join fls.group.user u " +
            "where u.username = :username")
    List<FirstLvlStatement> findAllByUser(String username);

    @Modifying
    @Transactional
    @Query(value = "update first_lvl_statements " +
            "set name = :newName " +
            "from users " +
            "where users.username = :username and first_lvl_statements.id = :id", nativeQuery = true)
    void renameStatement(Long id, String newName, String username);
}
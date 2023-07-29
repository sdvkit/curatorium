package com.sdv.kit.server.repository;

import com.sdv.kit.server.model.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @EntityGraph(attributePaths = {"students", "students.marks"})
    @Query(value = "select g from Group g " +
            "join fetch g.user " +
            "where g.user.username = :username")
    List<Group> findAllByUser(String username);

    @EntityGraph(attributePaths = {"students", "user", "firstLvlStatements"})
    @Query(value = "select g from Group g " +
            "join g.user " +
            "where g.user.username = :username and g.id = :id")
    Optional<Group> findByIdAndUser(Long id, String username);

    @Query(value = "select g from Group g " +
            "join g.user " +
            "where g.name = :name and g.groupYear = :groupYear and g.user.username = :username")
    Optional<Group> findExistsGroup(String name, Integer groupYear, String username);

    @Modifying
    @Transactional
    @Query(value = "update groups " +
            "set name = :newName " +
            "from users " +
            "where users.username = :username and groups.id = :id", nativeQuery = true)
    void renameGroup(Long id, String newName, String username);

    @Modifying
    @Transactional
    @Query(value = "update groups " +
            "set is_archived = not is_archived " +
            "from users " +
            "where users.username = :username and groups.id = :id", nativeQuery = true)
    void archiveGroup(Long id, String username);
}
package com.sdv.kit.server.repository;

import com.sdv.kit.server.model.Mark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkRepository extends JpaRepository<Mark, Long> {
}
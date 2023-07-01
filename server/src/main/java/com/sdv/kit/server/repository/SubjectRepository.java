package com.sdv.kit.server.repository;

import com.sdv.kit.server.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
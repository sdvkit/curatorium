package com.sdv.kit.server.repository;

import com.sdv.kit.server.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
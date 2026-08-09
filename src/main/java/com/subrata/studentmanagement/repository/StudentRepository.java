package com.subrata.studentmanagement.repository;

import com.subrata.studentmanagement.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // ============================================================
    // FIND STUDENT BY EMAIL
    // ============================================================
    Optional<Student> findByEmail(String email);

    // ============================================================
    // SEARCH BY NAME
    // ============================================================
    List<Student> findByNameContainingIgnoreCase(String name);

    // ============================================================
    // SEARCH BY DEPARTMENT
    // ============================================================
    List<Student> findByDepartmentIgnoreCase(String department);

    // ============================================================
    // COMBINED SEARCH
    // NAME + DEPARTMENT + PAGINATION + SORTING
    // ============================================================
    @Query("""
            SELECT s
            FROM Student s
            WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))
            AND LOWER(s.department) = LOWER(:department)
            """)
    Page<Student> search(
            @Param("name") String name,
            @Param("department") String department,
            Pageable pageable
    );
}
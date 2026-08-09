package com.subrata.studentmanagement.repository;

import com.subrata.studentmanagement.entity.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {

        studentRepository.deleteAll();

        Student subrata = new Student(
                "Subrata Mallick",
                "subrata2@gmail.com",
                "ECE",
                4,
                "9123456789"
        );

        Student rahul = new Student(
                "Rahul Kumar",
                "rahul@example.com",
                "CSE",
                4,
                "9876543210"
        );

        Student amit = new Student(
                "Amit Sharma",
                "amit@example.com",
                "CSE",
                3,
                "9876543211"
        );

        studentRepository.save(subrata);
        studentRepository.save(rahul);
        studentRepository.save(amit);
    }

    // ============================================================
    // FIND BY EMAIL
    // ============================================================

    @Test
    void findByEmail_shouldReturnStudent() {

        Optional<Student> result =
                studentRepository.findByEmail(
                        "subrata2@gmail.com"
                );

        assertTrue(result.isPresent());

        assertEquals(
                "Subrata Mallick",
                result.get().getName()
        );

        assertEquals(
                "ECE",
                result.get().getDepartment()
        );

        assertEquals(
                4,
                result.get().getYear()
        );
    }

    // ============================================================
    // FIND BY EMAIL - NOT FOUND
    // ============================================================

    @Test
    void findByEmail_shouldReturnEmptyForUnknownEmail() {

        Optional<Student> result =
                studentRepository.findByEmail(
                        "unknown@example.com"
                );

        assertTrue(result.isEmpty());
    }

    // ============================================================
    // SEARCH BY NAME
    // ============================================================

    @Test
    void findByNameContainingIgnoreCase_shouldFindStudents() {

        List<Student> result =
                studentRepository
                        .findByNameContainingIgnoreCase("subrata");

        assertEquals(1, result.size());

        assertEquals(
                "Subrata Mallick",
                result.get(0).getName()
        );
    }

    // ============================================================
    // SEARCH BY PARTIAL NAME
    // ============================================================

    @Test
    void findByNameContainingIgnoreCase_shouldSupportPartialSearch() {

        List<Student> result =
                studentRepository
                        .findByNameContainingIgnoreCase("rah");

        assertEquals(1, result.size());

        assertEquals(
                "Rahul Kumar",
                result.get(0).getName()
        );
    }

    // ============================================================
    // SEARCH BY DEPARTMENT
    // ============================================================

    @Test
    void findByDepartmentIgnoreCase_shouldFindStudents() {

        List<Student> result =
                studentRepository
                        .findByDepartmentIgnoreCase("cse");

        assertEquals(2, result.size());

        assertTrue(
                result.stream()
                        .allMatch(student ->
                                student.getDepartment()
                                        .equalsIgnoreCase("CSE"))
        );
    }

    // ============================================================
    // SEARCH BY UNKNOWN DEPARTMENT
    // ============================================================

    @Test
    void findByDepartmentIgnoreCase_shouldReturnEmptyForUnknownDepartment() {

        List<Student> result =
                studentRepository
                        .findByDepartmentIgnoreCase("ME");

        assertTrue(result.isEmpty());
    }

    // ============================================================
    // COMBINED SEARCH
    // NAME + DEPARTMENT
    // ============================================================

    @Test
    void search_shouldFindByNameAndDepartment() {

        Pageable pageable =
                PageRequest.of(0, 5);

        Page<Student> result =
                studentRepository.search(
                        "Subrata",
                        "ECE",
                        pageable
                );

        assertEquals(1, result.getTotalElements());

        assertEquals(1, result.getContent().size());

        assertEquals(
                "Subrata Mallick",
                result.getContent().get(0).getName()
        );
    }

    // ============================================================
    // COMBINED SEARCH - CASE INSENSITIVE
    // ============================================================

    @Test
    void search_shouldBeCaseInsensitive() {

        Pageable pageable =
                PageRequest.of(0, 5);

        Page<Student> result =
                studentRepository.search(
                        "SUBRATA",
                        "ece",
                        pageable
                );

        assertEquals(1, result.getTotalElements());

        assertEquals(
                "Subrata Mallick",
                result.getContent().get(0).getName()
        );
    }

    // ============================================================
    // COMBINED SEARCH - PAGINATION
    // ============================================================

    @Test
    void search_shouldSupportPagination() {

        Pageable pageable =
                PageRequest.of(0, 1);

        Page<Student> result =
                studentRepository.search(
                        "",
                        "CSE",
                        pageable
                );

        assertEquals(2, result.getTotalElements());

        assertEquals(1, result.getContent().size());

        assertEquals(2, result.getTotalPages());
    }

    // ============================================================
    // FIND ALL
    // ============================================================

    @Test
    void findAll_shouldReturnAllStudents() {

        List<Student> result =
                studentRepository.findAll();

        assertEquals(3, result.size());
    }

    // ============================================================
    // SAVE STUDENT
    // ============================================================

    @Test
    void save_shouldPersistStudent() {

        Student student = new Student(
                "New Student",
                "new@example.com",
                "IT",
                2,
                "9999999999"
        );

        Student saved =
                studentRepository.save(student);

        assertNotNull(saved.getId());

        Optional<Student> result =
                studentRepository.findById(saved.getId());

        assertTrue(result.isPresent());

        assertEquals(
                "New Student",
                result.get().getName()
        );
    }

    // ============================================================
    // DELETE STUDENT
    // ============================================================

    @Test
    void delete_shouldRemoveStudent() {

        Optional<Student> student =
                studentRepository.findByEmail(
                        "rahul@example.com"
                );

        assertTrue(student.isPresent());

        Long id = student.get().getId();

        studentRepository.deleteById(id);

        Optional<Student> result =
                studentRepository.findById(id);

        assertTrue(result.isEmpty());
    }
}
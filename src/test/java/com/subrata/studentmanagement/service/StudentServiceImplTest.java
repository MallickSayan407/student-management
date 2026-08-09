package com.subrata.studentmanagement.service;

import org.springframework.data.domain.Pageable;
import com.subrata.studentmanagement.dto.PageResponseDTO;
import com.subrata.studentmanagement.dto.StudentRequestDTO;
import com.subrata.studentmanagement.dto.StudentResponseDTO;
import com.subrata.studentmanagement.entity.Student;
import com.subrata.studentmanagement.exception.EmailAlreadyExistsException;
import com.subrata.studentmanagement.exception.StudentNotFoundException;
import com.subrata.studentmanagement.repository.StudentRepository;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentRequestDTO requestDTO;

    @BeforeEach
    void setUp() {

        student = new Student(
                "Subrata Mallick",
                "subrata2@gmail.com",
                "ECE",
                4,
                "9123456789"
        );

        student.setId(4L);

        requestDTO = new StudentRequestDTO(
                "Subrata Mallick",
                "subrata2@gmail.com",
                "ECE",
                4,
                "9123456789"
        );
    }

    // ============================================================
    // CREATE STUDENT
    // ============================================================

    @Test
    void saveStudent_shouldCreateStudentSuccessfully() {

        when(studentRepository.findByEmail(requestDTO.getEmail()))
                .thenReturn(Optional.empty());

        when(studentRepository.save(any(Student.class)))
                .thenReturn(student);

        StudentResponseDTO response =
                studentService.saveStudent(requestDTO);

        assertNotNull(response);
        assertEquals(4L, response.getId());
        assertEquals("Subrata Mallick", response.getName());
        assertEquals("subrata2@gmail.com", response.getEmail());
        assertEquals("ECE", response.getDepartment());
        assertEquals(4, response.getYear());
        assertEquals("9123456789", response.getPhone());

        verify(studentRepository).findByEmail(requestDTO.getEmail());
        verify(studentRepository).save(any(Student.class));
    }

    // ============================================================
    // DUPLICATE EMAIL
    // ============================================================

    @Test
    void saveStudent_shouldThrowExceptionForDuplicateEmail() {

        when(studentRepository.findByEmail(requestDTO.getEmail()))
                .thenReturn(Optional.of(student));

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> studentService.saveStudent(requestDTO)
        );

        verify(studentRepository).findByEmail(requestDTO.getEmail());
        verify(studentRepository, never()).save(any(Student.class));
    }

    // ============================================================
    // GET ALL STUDENTS
    // ============================================================

    @Test
    void getAllStudents_shouldReturnStudents() {

        Student secondStudent = new Student(
                "Rahul Kumar",
                "rahul@example.com",
                "CSE",
                4,
                "9123456789"
        );

        secondStudent.setId(11L);

        when(studentRepository.findAll())
                .thenReturn(List.of(student, secondStudent));

        List<StudentResponseDTO> result =
                studentService.getAllStudents();

        assertEquals(2, result.size());

        assertEquals("Subrata Mallick", result.get(0).getName());
        assertEquals("Rahul Kumar", result.get(1).getName());

        verify(studentRepository).findAll();
    }

    // ============================================================
    // GET STUDENT BY ID
    // ============================================================

    @Test
    void getStudentById_shouldReturnStudent() {

        when(studentRepository.findById(4L))
                .thenReturn(Optional.of(student));

        StudentResponseDTO response =
                studentService.getStudentById(4L);

        assertNotNull(response);
        assertEquals(4L, response.getId());
        assertEquals("Subrata Mallick", response.getName());

        verify(studentRepository).findById(4L);
    }

    // ============================================================
    // STUDENT NOT FOUND
    // ============================================================

    @Test
    void getStudentById_shouldThrowExceptionWhenStudentDoesNotExist() {

        when(studentRepository.findById(999L))
                .thenReturn(Optional.empty());

        StudentNotFoundException exception =
                assertThrows(
                        StudentNotFoundException.class,
                        () -> studentService.getStudentById(999L)
                );

        assertEquals(
                "Student with ID 999 not found",
                exception.getMessage()
        );

        verify(studentRepository).findById(999L);
    }

    // ============================================================
    // UPDATE STUDENT
    // ============================================================

    @Test
    void updateStudent_shouldUpdateSuccessfully() {

        StudentRequestDTO updateDTO =
                new StudentRequestDTO(
                        "Subrata Mallick",
                        "subrata2@gmail.com",
                        "CSE",
                        4,
                        "9123456789"
                );

        when(studentRepository.findById(4L))
                .thenReturn(Optional.of(student));

        when(studentRepository.findByEmail("subrata2@gmail.com"))
                .thenReturn(Optional.of(student));

        when(studentRepository.save(any(Student.class)))
                .thenReturn(student);

        StudentResponseDTO response =
                studentService.updateStudent(4L, updateDTO);

        assertEquals("CSE", response.getDepartment());
        assertEquals("9123456789", response.getPhone());

        verify(studentRepository).findById(4L);
        verify(studentRepository).findByEmail("subrata2@gmail.com");
        verify(studentRepository).save(student);
    }

    // ============================================================
    // UPDATE WITH ANOTHER STUDENT'S EMAIL
    // ============================================================

    @Test
    void updateStudent_shouldRejectDuplicateEmail() {

        Student anotherStudent = new Student(
                "Another Student",
                "another@gmail.com",
                "CSE",
                3,
                "9876543210"
        );

        anotherStudent.setId(8L);

        when(studentRepository.findById(4L))
                .thenReturn(Optional.of(student));

        when(studentRepository.findByEmail("another@gmail.com"))
                .thenReturn(Optional.of(anotherStudent));

        StudentRequestDTO updateDTO =
                new StudentRequestDTO(
                        "Subrata Mallick",
                        "another@gmail.com",
                        "CSE",
                        4,
                        "9123456789"
                );

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> studentService.updateStudent(4L, updateDTO)
        );

        verify(studentRepository, never()).save(any(Student.class));
    }

    // ============================================================
    // DELETE STUDENT
    // ============================================================

    @Test
    void deleteStudent_shouldDeleteSuccessfully() {

        when(studentRepository.existsById(4L))
                .thenReturn(true);

        studentService.deleteStudent(4L);

        verify(studentRepository).existsById(4L);
        verify(studentRepository).deleteById(4L);
    }

    // ============================================================
    // DELETE NON-EXISTING STUDENT
    // ============================================================

    @Test
    void deleteStudent_shouldThrowExceptionWhenStudentDoesNotExist() {

        when(studentRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.deleteStudent(999L)
        );

        verify(studentRepository, never()).deleteById(anyLong());
    }

    // ============================================================
    // SEARCH BY NAME
    // ============================================================

    @Test
    void searchByName_shouldReturnMatchingStudents() {

        when(studentRepository.findByNameContainingIgnoreCase("Subrata"))
                .thenReturn(List.of(student));

        List<StudentResponseDTO> result =
                studentService.searchByName("Subrata");

        assertEquals(1, result.size());
        assertEquals("Subrata Mallick", result.get(0).getName());

        verify(studentRepository)
                .findByNameContainingIgnoreCase("Subrata");
    }

    // ============================================================
    // SEARCH BY DEPARTMENT
    // ============================================================

    @Test
    void searchByDepartment_shouldReturnMatchingStudents() {

        when(studentRepository.findByDepartmentIgnoreCase("ECE"))
                .thenReturn(List.of(student));

        List<StudentResponseDTO> result =
                studentService.searchByDepartment("ECE");

        assertEquals(1, result.size());
        assertEquals("ECE", result.get(0).getDepartment());

        verify(studentRepository)
                .findByDepartmentIgnoreCase("ECE");
    }

    // ============================================================
    // PAGINATION
    // ============================================================

    @Test
    void getAllStudents_shouldReturnPaginatedResponse() {

        Page<Student> page =
                new PageImpl<>(
                        List.of(student),
                        PageRequest.of(0, 5),
                        1
                );

        when(studentRepository.findAll(any(PageRequest.class)))
                .thenReturn(page);

        PageResponseDTO response =
                studentService.getAllStudents(
                        0,
                        5,
                        "id",
                        "asc"
                );

        assertEquals(1, response.getContent().size());
        assertEquals(0, response.getPage());
        assertEquals(5, response.getSize());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());

        verify(studentRepository).findAll(any(PageRequest.class));
    }

    // ============================================================
    // INVALID PAGE
    // ============================================================

    @Test
    void getAllStudents_shouldRejectNegativePage() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> studentService.getAllStudents(
                                -1,
                                5,
                                "id",
                                "asc"
                        )
                );

        assertEquals(
                "Page number cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(studentRepository);
    }

    // ============================================================
    // INVALID PAGE SIZE
    // ============================================================

    @Test
    void getAllStudents_shouldRejectInvalidPageSize() {

        assertThrows(
                IllegalArgumentException.class,
                () -> studentService.getAllStudents(
                        0,
                        0,
                        "id",
                        "asc"
                )
        );

        verifyNoInteractions(studentRepository);
    }

    // ============================================================
    // PAGE SIZE > 100
    // ============================================================

    @Test
    void getAllStudents_shouldRejectPageSizeGreaterThan100() {

        assertThrows(
                IllegalArgumentException.class,
                () -> studentService.getAllStudents(
                        0,
                        101,
                        "id",
                        "asc"
                )
        );

        verifyNoInteractions(studentRepository);
    }

    // ============================================================
    // INVALID SORT FIELD
    // ============================================================

    @Test
    void getAllStudents_shouldRejectInvalidSortField() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> studentService.getAllStudents(
                                0,
                                5,
                                "invalid",
                                "asc"
                        )
                );

        assertEquals(
                "Invalid sort field: invalid",
                exception.getMessage()
        );

        verifyNoInteractions(studentRepository);
    }

    // ============================================================
    // INVALID SORT DIRECTION
    // ============================================================

    @Test
    void getAllStudents_shouldRejectInvalidSortDirection() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> studentService.getAllStudents(
                                0,
                                5,
                                "id",
                                "random"
                        )
                );

        assertEquals(
                "Sort direction must be either 'asc' or 'desc'",
                exception.getMessage()
        );

        verifyNoInteractions(studentRepository);
    }

    // ============================================================
    // COMBINED SEARCH
    // ============================================================

    @Test
    void search_shouldReturnPaginatedResults() {

        Page<Student> page =
                new PageImpl<>(
                        List.of(student),
                        PageRequest.of(0, 5),
                        1
                );

        when(studentRepository.search(
                eq("Subrata"),
                eq("ECE"),
                any(Pageable.class)
        )).thenReturn(page);

        PageResponseDTO response =
                studentService.search(
                        "Subrata",
                        "ECE",
                        0,
                        5,
                        "id",
                        "asc"
                );

        assertEquals(1, response.getContent().size());
        assertEquals(
                "Subrata Mallick",
                response.getContent().get(0).getName()
        );

        verify(studentRepository).search(
                eq("Subrata"),
                eq("ECE"),
                any(Pageable.class)
        );
    }
}
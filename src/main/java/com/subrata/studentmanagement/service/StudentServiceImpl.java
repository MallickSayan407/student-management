package com.subrata.studentmanagement.service;

import com.subrata.studentmanagement.dto.PageResponseDTO;
import com.subrata.studentmanagement.dto.StudentRequestDTO;
import com.subrata.studentmanagement.dto.StudentResponseDTO;
import com.subrata.studentmanagement.entity.Student;
import com.subrata.studentmanagement.exception.EmailAlreadyExistsException;
import com.subrata.studentmanagement.exception.StudentNotFoundException;
import com.subrata.studentmanagement.repository.StudentRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ============================================================
    // CREATE STUDENT
    // ============================================================

    @Override
    public StudentResponseDTO saveStudent(StudentRequestDTO studentDTO) {

        // Check duplicate email
        if (studentRepository.findByEmail(studentDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Student student = new Student();

        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setDepartment(studentDTO.getDepartment());
        student.setYear(studentDTO.getYear());
        student.setPhone(studentDTO.getPhone());

        Student savedStudent = studentRepository.save(student);

        return convertToResponseDTO(savedStudent);
    }

    // ============================================================
    // GET ALL STUDENTS
    // ============================================================

    @Override
    public List<StudentResponseDTO> getAllStudents() {

        return studentRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // ============================================================
    // GET ALL STUDENTS
    // PAGINATION + SORTING
    // ============================================================

    @Override
    public PageResponseDTO getAllStudents(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        validatePagination(page, size);
        validateSort(sortBy, direction);

        Pageable pageable = createPageable(
                page,
                size,
                sortBy,
                direction
        );

        Page<Student> studentPage =
                studentRepository.findAll(pageable);

        return convertToPageResponse(studentPage);
    }

    // ============================================================
    // GET STUDENT BY ID
    // ============================================================

    @Override
    public StudentResponseDTO getStudentById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"
                        )
                );

        return convertToResponseDTO(student);
    }

    // ============================================================
    // UPDATE STUDENT
    // ============================================================

    @Override
    public StudentResponseDTO updateStudent(
            Long id,
            StudentRequestDTO studentDTO
    ) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"
                        )
                );

        // Check whether email belongs to another student
        studentRepository.findByEmail(studentDTO.getEmail())
                .ifPresent(existingStudent -> {

                    if (!existingStudent.getId().equals(id)) {
                        throw new EmailAlreadyExistsException(
                                "Email already exists"
                        );
                    }
                });

        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setDepartment(studentDTO.getDepartment());
        student.setYear(studentDTO.getYear());
        student.setPhone(studentDTO.getPhone());

        Student updatedStudent =
                studentRepository.save(student);

        return convertToResponseDTO(updatedStudent);
    }

    // ============================================================
    // DELETE STUDENT
    // ============================================================

    @Override
    public void deleteStudent(Long id) {

        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(
                    "Student with ID " + id + " not found"
            );
        }

        studentRepository.deleteById(id);
    }

    // ============================================================
    // SEARCH BY NAME
    // ============================================================

    @Override
    public List<StudentResponseDTO> searchByName(String name) {

        return studentRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // ============================================================
    // SEARCH BY DEPARTMENT
    // ============================================================

    @Override
    public List<StudentResponseDTO> searchByDepartment(
            String department
    ) {

        return studentRepository
                .findByDepartmentIgnoreCase(department)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // ============================================================
    // COMBINED SEARCH
    // NAME + DEPARTMENT
    // PAGINATION + SORTING
    // ============================================================

    @Override
    public PageResponseDTO search(
            String name,
            String department,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        validatePagination(page, size);
        validateSort(sortBy, direction);

        Pageable pageable = createPageable(
                page,
                size,
                sortBy,
                direction
        );

        Page<Student> studentPage =
                studentRepository.search(
                        name,
                        department,
                        pageable
                );

        return convertToPageResponse(studentPage);
    }

    // ============================================================
    // PAGINATION VALIDATION
    // ============================================================

    private void validatePagination(
            int page,
            int size
    ) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative"
            );
        }

        if (size <= 0) {
            throw new IllegalArgumentException(
                    "Page size must be greater than 0"
            );
        }

        if (size > 100) {
            throw new IllegalArgumentException(
                    "Page size cannot be greater than 100"
            );
        }
    }

    // ============================================================
    // SORT VALIDATION
    // ============================================================

    private void validateSort(
            String sortBy,
            String direction
    ) {

        if (sortBy == null || sortBy.isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy
            );
        }

        List<String> allowedSortFields = List.of(
                "id",
                "name",
                "email",
                "department",
                "year",
                "phone"
        );

        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy
            );
        }

        if (direction == null ||
                (!direction.equalsIgnoreCase("asc")
                        && !direction.equalsIgnoreCase("desc"))) {

            throw new IllegalArgumentException(
                    "Sort direction must be either 'asc' or 'desc'"
            );
        }
    }

    // ============================================================
    // CREATE PAGEABLE
    // ============================================================

    private Pageable createPageable(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort;

        if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        return PageRequest.of(
                page,
                size,
                sort
        );
    }

    // ============================================================
    // ENTITY → RESPONSE DTO
    // ============================================================

    private StudentResponseDTO convertToResponseDTO(
            Student student
    ) {

        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getDepartment(),
                student.getYear(),
                student.getPhone()
        );
    }

    // ============================================================
    // PAGE → PAGE RESPONSE DTO
    // ============================================================

    private PageResponseDTO convertToPageResponse(
            Page<Student> studentPage
    ) {

        List<StudentResponseDTO> content =
                studentPage.getContent()
                        .stream()
                        .map(this::convertToResponseDTO)
                        .toList();

        return new PageResponseDTO(
                content,
                studentPage.getNumber(),
                studentPage.getSize(),
                studentPage.getTotalElements(),
                studentPage.getTotalPages()
        );
    }
}
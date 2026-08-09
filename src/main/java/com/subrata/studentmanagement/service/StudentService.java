package com.subrata.studentmanagement.service;

import com.subrata.studentmanagement.dto.PageResponseDTO;
import com.subrata.studentmanagement.dto.StudentRequestDTO;
import com.subrata.studentmanagement.dto.StudentResponseDTO;

import java.util.List;

public interface StudentService {

    // Create Student
    StudentResponseDTO saveStudent(StudentRequestDTO studentDTO);

    // Get All Students
    List<StudentResponseDTO> getAllStudents();

    // Get All Students - Pagination + Sorting
    PageResponseDTO getAllStudents(
            int page,
            int size,
            String sortBy,
            String direction
    );

    // Get Student By ID
    StudentResponseDTO getStudentById(Long id);

    // Update Student
    StudentResponseDTO updateStudent(
            Long id,
            StudentRequestDTO studentDTO
    );

    // Delete Student
    void deleteStudent(Long id);

    // Search By Name
    List<StudentResponseDTO> searchByName(String name);

    // Search By Department
    List<StudentResponseDTO> searchByDepartment(String department);

    // Combined Search + Pagination + Sorting
    PageResponseDTO search(
            String name,
            String department,
            int page,
            int size,
            String sortBy,
            String direction
    );
}
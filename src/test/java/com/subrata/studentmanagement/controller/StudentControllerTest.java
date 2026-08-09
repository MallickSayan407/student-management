package com.subrata.studentmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subrata.studentmanagement.dto.PageResponseDTO;
import com.subrata.studentmanagement.dto.StudentRequestDTO;
import com.subrata.studentmanagement.dto.StudentResponseDTO;
import com.subrata.studentmanagement.exception.EmailAlreadyExistsException;
import com.subrata.studentmanagement.exception.StudentNotFoundException;
import com.subrata.studentmanagement.service.StudentService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(com.subrata.studentmanagement.exception.GlobalExceptionHandler.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    // ============================================================
    // POST - CREATE STUDENT
    // ============================================================

    @Test
    void createStudent_shouldReturn201() throws Exception {

        StudentRequestDTO request = new StudentRequestDTO(
                "Subrata Mallick",
                "subrata2@gmail.com",
                "ECE",
                4,
                "9123456789"
        );

        StudentResponseDTO response = new StudentResponseDTO(
                1L,
                "Subrata Mallick",
                "subrata2@gmail.com",
                "ECE",
                4,
                "9123456789"
        );

        when(studentService.saveStudent(any(StudentRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Subrata Mallick"))
                .andExpect(jsonPath("$.email")
                        .value("subrata2@gmail.com"))
                .andExpect(jsonPath("$.department").value("ECE"))
                .andExpect(jsonPath("$.year").value(4))
                .andExpect(jsonPath("$.phone").value("9123456789"));
    }

    // ============================================================
    // POST - INVALID EMAIL
    // ============================================================

    @Test
    void createStudent_withInvalidEmail_shouldReturn400()
            throws Exception {

        StudentRequestDTO request = new StudentRequestDTO(
                "Subrata Mallick",
                "invalid-email",
                "ECE",
                4,
                "9123456789"
        );

        mockMvc.perform(
                        post("/api/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.email")
                        .value("Enter a valid email"));
    }

    // ============================================================
    // POST - INVALID PHONE
    // ============================================================

    @Test
    void createStudent_withInvalidPhone_shouldReturn400()
            throws Exception {

        StudentRequestDTO request = new StudentRequestDTO(
                "Subrata Mallick",
                "subrata@example.com",
                "ECE",
                4,
                "12345"
        );

        mockMvc.perform(
                        post("/api/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.phone")
                        .value("Phone number must contain exactly 10 digits"));
    }

    // ============================================================
    // POST - DUPLICATE EMAIL
    // ============================================================

    @Test
    void createStudent_withDuplicateEmail_shouldReturn409()
            throws Exception {

        StudentRequestDTO request = new StudentRequestDTO(
                "Another Student",
                "subrata2@gmail.com",
                "CSE",
                3,
                "9876543210"
        );

        when(studentService.saveStudent(any(StudentRequestDTO.class)))
                .thenThrow(
                        new EmailAlreadyExistsException(
                                "Email already exists"
                        )
                );

        mockMvc.perform(
                        post("/api/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error")
                        .value("Conflict"))
                .andExpect(jsonPath("$.message")
                        .value("Email already exists"));
    }

    // ============================================================
    // GET ALL STUDENTS
    // ============================================================

    @Test
    void getAllStudents_shouldReturn200() throws Exception {

        StudentResponseDTO student = new StudentResponseDTO(
                1L,
                "Subrata Mallick",
                "subrata2@gmail.com",
                "ECE",
                4,
                "9123456789"
        );

        PageResponseDTO response = new PageResponseDTO(
                List.of(student),
                0,
                5,
                1,
                1
        );

        when(studentService.getAllStudents(
                0,
                5,
                "id",
                "asc"
        )).thenReturn(response);

        mockMvc.perform(
                        get("/api/students")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sortBy", "id")
                                .param("direction", "asc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name")
                        .value("Subrata Mallick"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    // ============================================================
    // GET STUDENT BY ID
    // ============================================================

    @Test
    void getStudentById_shouldReturn200() throws Exception {

        StudentResponseDTO response = new StudentResponseDTO(
                4L,
                "Subrata Mallick",
                "subrata2@gmail.com",
                "CSE",
                4,
                "9123456789"
        );

        when(studentService.getStudentById(4L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/students/4")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name")
                        .value("Subrata Mallick"))
                .andExpect(jsonPath("$.department")
                        .value("CSE"));
    }

    // ============================================================
    // GET STUDENT BY ID - NOT FOUND
    // ============================================================

    @Test
    void getStudentById_whenNotFound_shouldReturn404()
            throws Exception {

        when(studentService.getStudentById(999L))
                .thenThrow(
                        new StudentNotFoundException(
                                "Student with ID 999 not found"
                        )
                );

        mockMvc.perform(
                        get("/api/students/999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Student with ID 999 not found"));
    }

    // ============================================================
    // SEARCH
    // ============================================================

    @Test
    void search_shouldReturn200() throws Exception {

        StudentResponseDTO student = new StudentResponseDTO(
                4L,
                "Subrata Mallick",
                "subrata2@gmail.com",
                "ECE",
                4,
                "9123456789"
        );

        PageResponseDTO response = new PageResponseDTO(
                List.of(student),
                0,
                5,
                1,
                1
        );

        when(studentService.search(
                "Subrata",
                "ECE",
                0,
                5,
                "id",
                "asc"
        )).thenReturn(response);

        mockMvc.perform(
                        get("/api/students/search")
                                .param("name", "Subrata")
                                .param("department", "ECE")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sortBy", "id")
                                .param("direction", "asc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name")
                        .value("Subrata Mallick"));
    }

    // ============================================================
    // SEARCH BY NAME
    // ============================================================

    @Test
    void searchByName_shouldReturn200() throws Exception {

        StudentResponseDTO student = new StudentResponseDTO(
                4L,
                "Subrata Mallick",
                "subrata2@gmail.com",
                "ECE",
                4,
                "9123456789"
        );

        when(studentService.searchByName("Subrata"))
                .thenReturn(List.of(student));

        mockMvc.perform(
                        get("/api/students/search/name")
                                .param("name", "Subrata")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                        .value("Subrata Mallick"))
                .andExpect(jsonPath("$[0].department")
                        .value("ECE"));
    }

    // ============================================================
    // SEARCH BY DEPARTMENT
    // ============================================================

    @Test
    void searchByDepartment_shouldReturn200() throws Exception {

        StudentResponseDTO student = new StudentResponseDTO(
                8L,
                "Rahul Kumar",
                "rahul@example.com",
                "CSE",
                4,
                "9123456789"
        );

        when(studentService.searchByDepartment("CSE"))
                .thenReturn(List.of(student));

        mockMvc.perform(
                        get("/api/students/search/department")
                                .param("department", "CSE")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                        .value("Rahul Kumar"))
                .andExpect(jsonPath("$[0].department")
                        .value("CSE"));
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Test
    void updateStudent_shouldReturn200() throws Exception {

        StudentRequestDTO request = new StudentRequestDTO(
                "Subrata Mallick",
                "subrata2@gmail.com",
                "CSE",
                4,
                "9123456789"
        );

        StudentResponseDTO response = new StudentResponseDTO(
                4L,
                "Subrata Mallick",
                "subrata2@gmail.com",
                "CSE",
                4,
                "9123456789"
        );

        when(studentService.updateStudent(
                eq(4L),
                any(StudentRequestDTO.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/students/4")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.department")
                        .value("CSE"))
                .andExpect(jsonPath("$.phone")
                        .value("9123456789"));
    }

    // ============================================================
    // UPDATE - NOT FOUND
    // ============================================================

    @Test
    void updateStudent_whenNotFound_shouldReturn404()
            throws Exception {

        StudentRequestDTO request = new StudentRequestDTO(
                "Subrata Mallick",
                "subrata2@gmail.com",
                "CSE",
                4,
                "9123456789"
        );

        when(studentService.updateStudent(
                eq(999L),
                any(StudentRequestDTO.class)
        )).thenThrow(
                new StudentNotFoundException(
                        "Student with ID 999 not found"
                )
        );

        mockMvc.perform(
                        put("/api/students/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Student with ID 999 not found"));
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Test
    void deleteStudent_shouldReturn204() throws Exception {

        doNothing()
                .when(studentService)
                .deleteStudent(4L);

        mockMvc.perform(
                        delete("/api/students/4")
                )
                .andExpect(status().isNoContent());
    }

    // ============================================================
    // DELETE - NOT FOUND
    // ============================================================

    @Test
    void deleteStudent_whenNotFound_shouldReturn404()
            throws Exception {

        doThrow(
                new StudentNotFoundException(
                        "Student with ID 999 not found"
                )
        )
                .when(studentService)
                .deleteStudent(999L);

        mockMvc.perform(
                        delete("/api/students/999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Student with ID 999 not found"));
    }
}
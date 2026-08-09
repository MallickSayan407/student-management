package com.subrata.studentmanagement.controller;

import com.subrata.studentmanagement.dto.PageResponseDTO;
import com.subrata.studentmanagement.dto.StudentRequestDTO;
import com.subrata.studentmanagement.dto.StudentResponseDTO;
import com.subrata.studentmanagement.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(
        name = "Student Management",
        description = "REST APIs for managing students including CRUD, validation, search, pagination and sorting"
)
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(
            summary = "Create a new student",
            description = "Creates a new student after validating the request and checking whether the email already exists."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Student created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StudentResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already exists",
                    content = @Content
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponseDTO saveStudent(
            @Valid @RequestBody StudentRequestDTO studentDTO) {

        return studentService.saveStudent(studentDTO);
    }

    @Operation(
            summary = "Get all students",
            description = "Retrieves all students using pagination and sorting."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Students retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination or sorting parameters",
                    content = @Content
            )
    })
    @GetMapping
    public PageResponseDTO getAllStudents(
            @Parameter(
                    description = "Zero-based page number",
                    example = "0"
            )
            @RequestParam(defaultValue = "0") int page,

            @Parameter(
                    description = "Number of students per page",
                    example = "5"
            )
            @RequestParam(defaultValue = "5") int size,

            @Parameter(
                    description = "Student field used for sorting",
                    example = "id"
            )
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(
                    description = "Sorting direction: asc or desc",
                    example = "asc"
            )
            @RequestParam(defaultValue = "asc") String direction) {

        return studentService.getAllStudents(
                page,
                size,
                sortBy,
                direction
        );
    }

    @Operation(
            summary = "Search students",
            description = "Searches students by name and department with pagination and sorting."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid search parameters",
                    content = @Content
            )
    })
    @GetMapping("/search")
    public PageResponseDTO search(
            @Parameter(
                    description = "Student name or part of the name",
                    example = "Subrata"
            )
            @RequestParam String name,

            @Parameter(
                    description = "Student department",
                    example = "ECE"
            )
            @RequestParam String department,

            @Parameter(
                    description = "Zero-based page number",
                    example = "0"
            )
            @RequestParam(defaultValue = "0") int page,

            @Parameter(
                    description = "Number of students per page",
                    example = "5"
            )
            @RequestParam(defaultValue = "5") int size,

            @Parameter(
                    description = "Field used for sorting",
                    example = "id"
            )
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(
                    description = "Sorting direction: asc or desc",
                    example = "asc"
            )
            @RequestParam(defaultValue = "asc") String direction) {

        return studentService.search(
                name,
                department,
                page,
                size,
                sortBy,
                direction
        );
    }

    @Operation(
            summary = "Search students by name",
            description = "Returns students whose names contain the supplied text. Search is case-insensitive."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Students found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "array",
                                    implementation = StudentResponseDTO.class
                            )
                    )
            )
    })
    @GetMapping("/search/name")
    public List<StudentResponseDTO> searchByName(
            @Parameter(
                    description = "Name or part of the name",
                    example = "Subrata"
            )
            @RequestParam String name) {

        return studentService.searchByName(name);
    }

    @Operation(
            summary = "Search students by department",
            description = "Returns students belonging to the specified department. Search is case-insensitive."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Students found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "array",
                                    implementation = StudentResponseDTO.class
                            )
                    )
            )
    })
    @GetMapping("/search/department")
    public List<StudentResponseDTO> searchByDepartment(
            @Parameter(
                    description = "Department name",
                    example = "ECE"
            )
            @RequestParam String department) {

        return studentService.searchByDepartment(department);
    }

    @Operation(
            summary = "Get student by ID",
            description = "Retrieves a single student using their unique ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Student found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StudentResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public StudentResponseDTO getStudentById(
            @Parameter(
                    description = "Unique student ID",
                    example = "2"
            )
            @PathVariable Long id) {

        return studentService.getStudentById(id);
    }

    @Operation(
            summary = "Update a student",
            description = "Updates an existing student's information after validation."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Student updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StudentResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already exists",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public StudentResponseDTO updateStudent(
            @Parameter(
                    description = "Unique student ID",
                    example = "2"
            )
            @PathVariable Long id,

            @Valid @RequestBody StudentRequestDTO studentDTO) {

        return studentService.updateStudent(id, studentDTO);
    }

    @Operation(
            summary = "Delete a student",
            description = "Deletes an existing student using their unique ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Student deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(
            @Parameter(
                    description = "Unique student ID",
                    example = "2"
            )
            @PathVariable Long id) {

        studentService.deleteStudent(id);
    }
}
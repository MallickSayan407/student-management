package com.subrata.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class StudentRequestDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Department cannot be empty")
    private String department;

    @Min(value = 1, message = "Year must be at least 1")
    @Max(value = 4, message = "Year cannot be greater than 4")
    private Integer year;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    private String phone;

    public StudentRequestDTO() {
    }

    public StudentRequestDTO(
            String name,
            String email,
            String department,
            Integer year,
            String phone
    ) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.year = year;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
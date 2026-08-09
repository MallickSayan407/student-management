package com.subrata.studentmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "students",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_email",
                        columnNames = "email"
                )
        }
)
public class Student {

    // ============================================================
    // PRIMARY KEY
    // ============================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ============================================================
    // STUDENT NAME
    // ============================================================

    @Column(nullable = false)
    private String name;

    // ============================================================
    // EMAIL
    // ============================================================

    @Column(nullable = false, unique = true)
    private String email;

    // ============================================================
    // DEPARTMENT
    // ============================================================

    @Column(nullable = false)
    private String department;

    // ============================================================
    // YEAR
    // IMPORTANT:
    // Database column is student_year instead of year
    // because YEAR can cause SQL/H2 keyword conflicts.
    // Java property remains "year".
    // ============================================================

    @Column(name = "student_year")
    private Integer year;

    // ============================================================
    // PHONE
    // ============================================================

    @Column(length = 10)
    private String phone;

    // ============================================================
    // DEFAULT CONSTRUCTOR
    // ============================================================

    public Student() {
    }

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public Student(
            String name,
            String email,
            String department,
            Integer year,
            String phone) {

        this.name = name;
        this.email = email;
        this.department = department;
        this.year = year;
        this.phone = phone;
    }

    // ============================================================
    // GETTERS AND SETTERS
    // ============================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
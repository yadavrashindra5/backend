package com.springboot.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="jpa_student")
public class Student {
    @Id
    private int studentId;
    private String studentName;
    private String about;

    @OneToMany(mappedBy="student")
    private List<Laptop> laptop;
}

package com.springboot.entities;

import jakarta.persistence.*;

@Entity
@Table(name="jpa_laptop")
public class Laptop {
    @Id
    private int laptopId;
    private String modelNumber;
    private String brand;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

}

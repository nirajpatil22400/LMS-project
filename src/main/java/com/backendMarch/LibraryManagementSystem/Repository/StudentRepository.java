package com.backendMarch.LibraryManagementSystem.Repository;

import com.backendMarch.LibraryManagementSystem.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Integer> {

    // findBy{attribute name}
    Student findByEmail(String email);  // custom search based on attribute
    List<Student> findByAge(int age);
}

package com.backendMarch.LibraryManagementSystem.Repository;

import com.backendMarch.LibraryManagementSystem.Entity.LibraryCard;
import com.backendMarch.LibraryManagementSystem.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardRepository extends JpaRepository<LibraryCard,Integer> {
}

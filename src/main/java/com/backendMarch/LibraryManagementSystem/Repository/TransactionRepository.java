package com.backendMarch.LibraryManagementSystem.Repository;

import com.backendMarch.LibraryManagementSystem.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    @Query(value = "select * from transaction t where t.card_card_no=:cardId AND t.transaction_status='SUCCESS'",
            nativeQuery = true)
    List<Transaction> getAllSuccessfullTxnsWithCardNo(int cardId);
}

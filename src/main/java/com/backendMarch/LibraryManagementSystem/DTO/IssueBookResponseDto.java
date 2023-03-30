package com.backendMarch.LibraryManagementSystem.DTO;


import com.backendMarch.LibraryManagementSystem.Enum.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueBookResponseDto {

    private String transanctionId;

    private String bookName;

    private TransactionStatus transactionStatus;
}

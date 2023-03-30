package com.backendMarch.LibraryManagementSystem.Service;

import com.backendMarch.LibraryManagementSystem.DTO.IssueBookRequestDto;
import com.backendMarch.LibraryManagementSystem.DTO.IssueBookResponseDto;
import com.backendMarch.LibraryManagementSystem.Entity.Book;
import com.backendMarch.LibraryManagementSystem.Entity.LibraryCard;
import com.backendMarch.LibraryManagementSystem.Entity.Transaction;
import com.backendMarch.LibraryManagementSystem.Enum.CardStatus;
import com.backendMarch.LibraryManagementSystem.Enum.TransactionStatus;
import com.backendMarch.LibraryManagementSystem.Repository.BookRepository;
import com.backendMarch.LibraryManagementSystem.Repository.CardRepository;
import com.backendMarch.LibraryManagementSystem.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private JavaMailSender emailSender;

    public IssueBookResponseDto issueBook(IssueBookRequestDto issueBookRequestDto) throws Exception {

        //create a transaction Object
        Transaction transaction = new Transaction();
        transaction.setTransactionNumber(String.valueOf(UUID.randomUUID()));
        transaction.setIssueOperation(true);


        LibraryCard card;
        try{
            //step1
           card = cardRepository.findById(issueBookRequestDto.getCardId()).get();
        }
        catch (Exception e){
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transaction.setMessage("Invalid card id");
            transactionRepository.save(transaction);
            throw new Exception("Invalid card id");
        }

        Book book;

        try{
            book = bookRepository.findById(issueBookRequestDto.getBookId()).get();
        }
        catch (Exception e){
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transaction.setMessage("Invalid card id");
            transactionRepository.save(transaction);
            throw new Exception("Invalid Book Id");
        }

        // both and card and book are valid
        transaction.setBook(book);
        transaction.setCard(card);

        if(card.getStatus() != CardStatus.ACTIVATED){
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transaction.setMessage("Your card is not activated");
            transactionRepository.save(transaction);
            throw new Exception("Your card is not activated");
        }

        if(book.isIssued()==true){
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transaction.setMessage("Sorry! Book is already issued");
            transactionRepository.save(transaction);
            throw new Exception("Sorry! Book is already issued");
        }

        // I can issue the book
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setMessage("Transaction was succesfull");

        book.setIssued(true);
        book.setCard(card);
        book.getTransaction().add(transaction);
        card.getTransactionList().add(transaction);
        card.getBooksIssued().add(book);

        cardRepository.save(card); //will ave book and transaction also

        //prepare Response Dto
        IssueBookResponseDto issueBookResponseDto = new IssueBookResponseDto();
        issueBookResponseDto.setTransanctionId(transaction.getTransactionNumber());
        issueBookResponseDto.setTransactionStatus(TransactionStatus.SUCCESS);
        issueBookResponseDto.setBookName(book.getTitle());



        // send an email
        String text = " Congrats !!." + card.getStudent().getName()+ " You have been issued "+book.getTitle()+" book. ";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("backendavengers7022@gmail.com");
        message.setTo(card.getStudent().getEmail());
        message.setSubject("Issue Book Notification");
        message.setText(text);
        emailSender.send(message);


        return issueBookResponseDto;

    }

    public String getAllTxns(int cardId){
        List<Transaction> transactionList = transactionRepository.getAllSuccessfullTxnsWithCardNo(cardId);
        String ans = "";
        for(Transaction t: transactionList){
            ans += t.getTransactionNumber();
            ans += "\n";
        }

        return ans;
    }

}

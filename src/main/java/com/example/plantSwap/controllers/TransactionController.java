package com.example.plantSwap.controllers;


import com.example.plantSwap.models.Transaction;
import com.example.plantSwap.repositories.PlantRepository;
import com.example.plantSwap.repositories.TransactionRepository;
import com.example.plantSwap.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlantRepository plantRepository;

    @PostMapping
    public ResponseEntity<Transaction> createUser(@RequestBody Transaction transaction) {
        if (transaction.getPlant().isTrade() && transaction.getTradePlant() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This plant must be traded");
        } else if (!transaction.getPlant().isTrade() && transaction.getTradePlant() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This plant cannot be traded");
        }

        if (transaction.getPlant().isTrade()){
            transaction.getPlant().setStatus("Traded");
            transaction.getTradePlant().setStatus("Traded");
            plantRepository.save(transaction.getPlant());
            plantRepository.save(transaction.getTradePlant());
        } else {
            transaction.getPlant().setStatus("Bought");
            plantRepository.save(transaction.getPlant());
        }

        Transaction savedTransaction = transactionRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateUser(@PathVariable String id, @RequestBody Transaction transactionDetails) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        existingTransaction.setUser(transactionDetails.getUser());
        existingTransaction.setPlant(transactionDetails.getPlant());
        existingTransaction.setTradePlant(transactionDetails.getTradePlant());

        return ResponseEntity.ok(transactionRepository.save(existingTransaction));
    }
}

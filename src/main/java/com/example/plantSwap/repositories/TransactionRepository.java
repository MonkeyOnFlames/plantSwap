package com.example.plantSwap.repositories;

import com.example.plantSwap.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
}

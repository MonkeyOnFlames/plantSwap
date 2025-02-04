package com.example.plantSwap.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Transaction {
    @Id
    private String id;
    @DBRef
    private User user;
    @DBRef
    private Plant plant;
}

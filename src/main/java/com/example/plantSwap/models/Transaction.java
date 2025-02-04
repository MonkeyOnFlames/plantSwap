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
    @DBRef
    private Plant tradePlant;

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Plant getTradePlant() {
        return tradePlant;
    }

    public void setTradePlant(Plant tradePlant) {
        this.tradePlant = tradePlant;
    }
}

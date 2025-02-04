package com.example.plantSwap.repositories;

import com.example.plantSwap.models.Plant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlantRepository extends MongoRepository<Plant, String> {
}

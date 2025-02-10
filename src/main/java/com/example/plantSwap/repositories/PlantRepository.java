package com.example.plantSwap.repositories;

import com.example.plantSwap.models.Plant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlantRepository extends MongoRepository<Plant, String> {
    List<Plant> findByUserId(String id);
    List<Plant> findByStatus(String status);
    Plant findPlantById(String id);
}

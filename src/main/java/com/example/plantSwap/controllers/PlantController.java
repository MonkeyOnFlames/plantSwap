package com.example.plantSwap.controllers;

import com.example.plantSwap.models.Plant;
import com.example.plantSwap.repositories.PlantRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

        @Autowired
        private PlantRepository plantRepository;

        @PostMapping
        public ResponseEntity<Plant> createPlant(@Valid @RequestBody Plant plant) {
            List<Plant> userAdds = plantRepository.findByUserId(plant.getUser().getId());
            int i = 0;
            for (Plant plantCheck : userAdds) {
                if (Objects.equals(plantCheck.getStatus(), "available")) {
                    i++;
                }
            }
            if (i >= 10){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot have more than 10 available plants");
            }
            Plant savedPlant = plantRepository.save(plant);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedPlant);
        }

        @GetMapping
        public ResponseEntity<List<Plant>> getAllPlants() {
            List<Plant> plants = plantRepository.findAll();
            return ResponseEntity.ok(plants);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Plant> getPlantById(@PathVariable String id) {
            Plant plant = plantRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plant not found"));
            return ResponseEntity.ok(plant);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Plant> updatePlant(@Valid @PathVariable String id, @RequestBody Plant plantDetails) {
            Plant existingPlant = plantRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plant not found"));

            existingPlant.setName(plantDetails.getName());
            existingPlant.setScientificName(plantDetails.getScientificName());
            existingPlant.setAge(plantDetails.getAge());
            existingPlant.setSize(plantDetails.getSize());
            existingPlant.setType(plantDetails.getType());
            existingPlant.setLightNeeds(plantDetails.getLightNeeds());
            existingPlant.setWaterNeeds(plantDetails.getWaterNeeds());
            existingPlant.setDifficulty(plantDetails.getDifficulty());
            existingPlant.setTrade(plantDetails.isTrade());
            existingPlant.setPrice(plantDetails.getPrice());
            existingPlant.setPictureUrl(plantDetails.getPictureUrl());
            existingPlant.setStatus(plantDetails.getStatus());


            return ResponseEntity.ok(plantRepository.save(existingPlant));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Plant> deletePlant(@PathVariable String id) {
            if (!plantRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plant not found");
            }

            plantRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }

        @GetMapping("/available")
        public ResponseEntity<List<Plant>> getAvailablePlants() {
            String available = "available";
            List<Plant> availablePlants = plantRepository.findByStatus(available);
            return ResponseEntity.ok(availablePlants);
        }


}

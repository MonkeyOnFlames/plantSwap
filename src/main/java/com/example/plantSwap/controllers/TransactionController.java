package com.example.plantSwap.controllers;


import com.example.plantSwap.models.Plant;
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
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        // when a plant is sent into transaction, all it details becomes null, 0 or false, making it impossible to check for trade
        // Want to add if transaction.getTradePlant() is empty, but do not know how

        if (transaction.getPlant() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plant is required");
        }

        Plant existingPlant = plantRepository.findPlantById(transaction.getPlant().getId());
        boolean originalPlantIsTrade = existingPlant.isTrade();

        Plant existingTradePlant = null;


        if (originalPlantIsTrade){
            if (transaction.getTradePlant() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tradePlant is required");
            }
            existingTradePlant = plantRepository.findPlantById(transaction.getTradePlant().getId());
        }



        if (originalPlantIsTrade){
            existingPlant.setStatus("pending, waiting approval");

            existingTradePlant.setStatus("pending, waiting approval");

            transaction.setTradePlant(existingTradePlant);

            plantRepository.save(transaction.getTradePlant());
        } else {
            existingPlant.setStatus("bought");
            existingPlant.setUser(transaction.getUser());
        }


        transaction.setPlant(existingPlant);

        plantRepository.save(existingPlant);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }

    // exempel på förbättrad createTransaction metod
    @PostMapping("/new")
    public ResponseEntity<Transaction> newCreateTransaction(@RequestBody Transaction transaction) {
        // grundläggande validering först
        if (transaction.getPlant() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plant is required");
        }

        // använd Optional och förbättrad felhantering
        // findById returnerar en Optional<Plant> vilket är en säkrare hantering av möjliga null-värden
        // med .orElseThrow() hanterar vi direkt situationen där växten inte hittas
        // på så sätt slipper vi separata null-kontroller efter att ha hämtat växten
        Plant existingPlant = plantRepository.findById(transaction.getPlant().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Plant with ID " + transaction.getPlant().getId() + " not found"));

        boolean originalPlantIsTrade = existingPlant.isTrade();
        Plant existingTradePlant = null;

        // kontrollera att växten är tillgänglig
        if (!existingPlant.getStatus().equals("available")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Plant is not available for transaction");
        }

        // validera att bytesväxt finns om det behövs
        if (originalPlantIsTrade) {
            if (transaction.getTradePlant() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tradePlant is required");
            }

            existingTradePlant = plantRepository.findById(transaction.getTradePlant().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Trade plant with ID " + transaction.getTradePlant().getId() + " not found"));

            // kontrollera att bytesväxten är tillgänglig
            if (!existingTradePlant.getStatus().equals("available")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Trade plant is not available for transaction");
            }

            // uppdatera status för bytesväxter
            existingPlant.setStatus("pending, waiting approval");
            existingTradePlant.setStatus("pending, waiting approval");

            transaction.setTradePlant(existingTradePlant);
            plantRepository.save(existingTradePlant);
        } else {
            // uppdatera status för vanligt köp
            existingPlant.setStatus("bought");
            existingPlant.setUser(transaction.getUser());
        }

        transaction.setPlant(existingPlant);
        plantRepository.save(existingPlant);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);

        // förbättringarna inkluderar:
        // 1. användning av plantRepository.findById() med Optional och korrekt felhantering
        // 2. kontroll att växten faktiskt är tillgänglig innan transaktion
        // 3. tydligare felmeddelanden som innehåller ID på växten som inte hittades
        // 4. bättre strukturerad kod med validering först, sedan logik

        // eventuellt får du kanske justera lite för jag har inte hunnit testa :)
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
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String id, @RequestBody Transaction transactionDetails) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        existingTransaction.setUser(transactionDetails.getUser());
        existingTransaction.setPlant(transactionDetails.getPlant());
        existingTransaction.setTradePlant(transactionDetails.getTradePlant());

        return ResponseEntity.ok(transactionRepository.save(existingTransaction));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>>  getTransactionByUserId(@PathVariable String userId) {

        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}

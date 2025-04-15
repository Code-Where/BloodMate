package com.asdeveloper.BloodMate.UsersHealth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/health-info")
public class HealthController {
    @Autowired
    private HealthService service;

    @GetMapping
    public List<Model> getAllRecords() {
        return service.getAllRecords();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Model> getRecordById(@PathVariable Long id) {
        Optional<Model> record = service.getRecordById(id);
        return record.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public Model getRecordsByUserId(@PathVariable Long userId) {
        return service.getRecordsByUserId(userId);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Model> createRecord(@PathVariable Long userId, @RequestBody Model record) {
        return ResponseEntity.ok(service.saveRecord(record, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Model> updateRecord(@PathVariable Long id, @RequestBody Model record) {
        if (!service.getRecordById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        record.setUser(service.getRecordById(id).get().getUser());
        record.setId(id);
        return ResponseEntity.ok(service.saveRecord(record, record.getUser().getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        if (!service.getRecordById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}

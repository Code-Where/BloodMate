package com.asdeveloper.BloodMate.UsersHealth;
import org.springframework.stereotype.Service;

import com.asdeveloper.BloodMate.Users.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class HealthService {

    private final HealthRepository repository;
    private final UserRepository userRepository;

    public HealthService(HealthRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<Model> getAllRecords() {
        return repository.findAll();
    }

    public Optional<Model> getRecordById(Long id) {
        return repository.findById(id);
    }

    public Model getRecordsByUserId(Long userId) {
        Optional<com.asdeveloper.BloodMate.Users.User> user = userRepository.findById(userId);
        return user.map(repository::findByUser).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Model saveRecord(Model record, Long userId) {
        com.asdeveloper.BloodMate.Users.User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        record.setUser(user);
        return repository.save(record);
    }

    public void deleteRecord(Long id) {
        repository.deleteById(id);
    }
}

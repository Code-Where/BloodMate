package com.asdeveloper.BloodMate.Users;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;



@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUser(){
        java.util.List<User> users = userService.getAllUser();
        if (users.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(userService.getAllUser());
    }
    @GetMapping("/phone")
    public ResponseEntity<?> getUserByPhone(@RequestParam String phone) {
        User user = userService.getUserByPhone(phone);
        if (user == null) {
            return ResponseEntity.ok(Collections.emptyMap());
        }
        return ResponseEntity.ok(userService.getUserByPhone(phone));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.ok(Collections.emptyMap());
        }
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User entity) {        
        return userService.updateUser(id, entity);
    }

    @PutMapping("/changePassword/{phone}")
    public User updatePassword(@PathVariable String phone, @RequestBody String password) {
        return userService.updateUserPassword(phone, password);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }
}

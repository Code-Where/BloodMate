package com.asdeveloper.BloodMate.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public java.util.List<User> getAllUser(){
        return userRepository.findAll();
    } 

    public User getUserByPhone(String phoneNum){
        return userRepository.findByNumber(phoneNum);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user){
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setPhone(user.getPhone());
            existingUser.setName(user.getName());
            existingUser.setPassword(user.getPassword());
            existingUser.setDob(user.getDob());
            existingUser.setGender(user.getGender());
            existingUser.setBloodgroup(user.getBloodgroup());
            existingUser.setEmailid(user.getEmailid());
            existingUser.setLastdonation(user.getLastdonation());
            return userRepository.save(existingUser);
        }
        return null; // User Not Found
    }
    

    public User updateUserPassword(String phone, String password){
        User exisitingUser = userRepository.findByNumber(phone);
        if (exisitingUser != null) {
            exisitingUser.setPassword(password);
            return userRepository.save(exisitingUser);
        }
        return null; // User Not Found
    }

    public String deleteUser(Long id){
        User existUser = userRepository.findById(id).orElse(null);
        if (existUser != null) {
            userRepository.delete(existUser);
            
            return "Deleted Successfully";
        }
        return null; // User Not Found
    }
}

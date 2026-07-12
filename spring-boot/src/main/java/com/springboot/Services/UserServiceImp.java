package com.springboot.Services;

import com.springboot.entities.User;
import com.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public User updateUser(User user, int id) {
        return null;
    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public User getUserById(int id) {
        User user= userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> all = userRepository.findAll();
        return all;
    }
}

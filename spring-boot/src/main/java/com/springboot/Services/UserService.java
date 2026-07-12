package com.springboot.Services;

import com.springboot.entities.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User updateUser(User user,int id);
    void deleteUser(int id);
    User getUserById(int id);
    List<User> getAllUsers();
}

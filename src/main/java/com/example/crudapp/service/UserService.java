package com.example.crudapp.service;

import com.example.crudapp.Entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Integer id);
    User save(User theUser);
    void delete(Integer id);
    User findByEmail(String email);
    User findByPassword(String password);
}

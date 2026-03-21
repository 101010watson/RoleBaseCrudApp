package com.example.crudapp.dao;

import com.example.crudapp.Entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmail(String Email);
    User findByPassword(String password);
}

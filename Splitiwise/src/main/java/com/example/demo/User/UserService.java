package com.example.demo.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User saveUser(User user) {

        return userRepo.save(user);

    }

    public boolean loginUser(String email, String password) {

        User user = userRepo.findByEmail(email);

        if(user != null && user.getPassword().equals(password)){

            return true;

        }

        return false;

    }

    public boolean existsByEmail(String email){

        return userRepo.findByEmail(email) != null;

    }

    public User findByEmail(String email){

        return userRepo.findByEmail(email);

    }

}
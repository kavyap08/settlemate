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

        List<User> users = userRepo.findByEmail(email);

        if(users.size() > 0) {

            User user = users.get(0);

            if(user.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }


  
    public boolean existsByEmail(String email) {

        List<User> users = userRepo.findByEmail(email);

        return users.size() > 0;
    }
    public List<User> findByEmail(String email){
        return userRepo.findByEmail(email);
    }
}

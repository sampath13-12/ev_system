package com.ev_centers.project.service;

import com.ev_centers.project.entity.EvOwner;
import com.ev_centers.project.entity.User;
import com.ev_centers.project.repository.EvOwnerRepository;
import com.ev_centers.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private EvOwnerRepository evOwnerRepository;

    @Autowired
    private UserRepository userRepository;

    public User register(User user){
        return userRepository.save(user);
    }

    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    public List<EvOwner> findAllCenters() {
        return evOwnerRepository.findAll();
    }
}

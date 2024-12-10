package com.ev_centers.project.service;


import com.ev_centers.project.entity.EvOwner;
import com.ev_centers.project.repository.EvOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EvOwnerService {

    @Autowired
    private EvOwnerRepository evOwnerRepository;

    public Object login(String username, String password) {
        EvOwner owner = evOwnerRepository.findByUsername(username);
        if (owner.getUsername()!= null && owner.getPassword().equals(password)) {
            return owner;
        }
        return Optional.empty();
    }
}

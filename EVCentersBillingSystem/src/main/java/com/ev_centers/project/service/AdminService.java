package com.ev_centers.project.service;

import com.ev_centers.project.entity.EvOwner;
import com.ev_centers.project.repository.EvOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private EvOwnerRepository evOwnerRepository;

    public EvOwner addEvowner(EvOwner evOwner){
        return evOwnerRepository.save(evOwner);
    }




}

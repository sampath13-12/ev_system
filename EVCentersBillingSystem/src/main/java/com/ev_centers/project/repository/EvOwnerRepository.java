package com.ev_centers.project.repository;

import com.ev_centers.project.entity.EvOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvOwnerRepository extends JpaRepository<EvOwner,Long> {

    EvOwner findByUsername(String username);
}

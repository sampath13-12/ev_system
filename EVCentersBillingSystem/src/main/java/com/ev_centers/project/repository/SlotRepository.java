package com.ev_centers.project.repository;

import com.ev_centers.project.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotRepository extends JpaRepository<Slot,Long> {
}

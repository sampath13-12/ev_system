package com.ev_centers.project.service;

import com.ev_centers.project.entity.Slot;
import com.ev_centers.project.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotService {
    @Autowired
    private SlotRepository slotRepository;

    public Slot getSlotById(Long slotId) {
        return slotRepository.findById(slotId).orElse(null);
    }

    public Slot saveSlot(Slot newSlot) {
        return slotRepository.save(newSlot);
    }

    public List<Slot> getSlots(){
        return slotRepository.findAll();
    }

    public Slot updatePaymentStatus(Long slotId, String paymentStatus) {
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new RuntimeException("Slot not found"));
        slot.setPaymentStatus(paymentStatus);
        return slotRepository.save(slot);
    }
}

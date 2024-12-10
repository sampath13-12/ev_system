package com.ev_centers.project.controller;


import com.ev_centers.project.entity.Booking;
import com.ev_centers.project.entity.Slot;
import com.ev_centers.project.service.BookingService;
import com.ev_centers.project.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/slots")
public class SlotBookingController {


    @Autowired
    private SlotService slotService;

    @Autowired
    private BookingService bookingService;


    @PostMapping("/book-slot")
    public ResponseEntity<?> bookSlot(@RequestParam String username, @RequestParam Long slotId) {
        HashMap<String, Object> response = new HashMap<>();
        Slot slot = slotService.getSlotById(slotId);

        if (slot == null) {
            response.put("msg","Slot not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Booking booking = new Booking();
        booking.setUsername(username);
        booking.setSlot(slot);
        booking.setStatus("PENDING");
        bookingService.saveBooking(booking);

        return ResponseEntity.status(HttpStatus.CREATED).body("Booking request created successfully");
    }





}

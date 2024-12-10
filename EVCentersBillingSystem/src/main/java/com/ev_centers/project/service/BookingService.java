package com.ev_centers.project.service;

import com.ev_centers.project.entity.Booking;
import com.ev_centers.project.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }

//    public List<Booking> getPendingRequests(Long evOwnerId) {
//        return bookingRepository.findByEvOwnerIdAndStatus(evOwnerId, "PENDING");
//    }
//
//    public Booking getBookingById(Long bookingId) {
//        return bookingRepository.findById(bookingId).orElse(null);
//    }
//
    public Booking saveBookingRequest(Booking booking) {
        return bookingRepository.save(booking);
    }
//
//    public Booking findBookingRequestById(Long bookingRequestId) {
//        return bookingRepository.findById(bookingRequestId).orElseThrow(() -> new RuntimeException("Booking not found"));
//    }
}

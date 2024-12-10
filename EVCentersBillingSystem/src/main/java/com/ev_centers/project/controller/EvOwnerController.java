package com.ev_centers.project.controller;

import com.ev_centers.project.entity.Booking;
import com.ev_centers.project.entity.EvOwner;
import com.ev_centers.project.entity.Slot;
import com.ev_centers.project.repository.BookingRepository;
import com.ev_centers.project.repository.EvOwnerRepository;
import com.ev_centers.project.service.BookingService;
import com.ev_centers.project.service.EvOwnerService;
import com.ev_centers.project.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/owner")
public class EvOwnerController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private SlotService slotService;

    @Autowired
    private EvOwnerRepository evOwnerRepository;

    @Autowired
    private EvOwnerService evOwnerService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody HashMap<String, String> loginRequest) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                response.put("msg", "Enter the required fields");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if ("Admin".equals(username) && "admin123".equals(password)) {
                response.put("msg", "Login successful");
                response.put("username", username);
                return ResponseEntity.ok(response);
            }

            EvOwner evOwner = evOwnerRepository.findByUsername(username);
            if (evOwner != null) {
                // Check if the password matches for the EvOwner (plain-text comparison)
                if (password.equals(evOwner.getPassword())) {
                    response.put("msg", "Login successful");
                    response.put("username", evOwner.getUsername());
                    response.put("email", evOwner.getEmail());
                    return ResponseEntity.ok(response);
                } else {
                    response.put("msg", "Invalid password");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } else {
                response.put("msg", "User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("msg", "An error occurred during login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/view-slots")
    public ResponseEntity<?> viewSlots(@RequestParam String location, @RequestParam(required = false) String status) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            List<Slot> slots = slotService.getSlots();

            List<Slot> filteredSlots = slots.stream()
                    .filter(slot -> slot.getLocation().equalsIgnoreCase(location) &&
                            (status == null || slot.getStatus().equalsIgnoreCase(status)))
                    .collect(Collectors.toList());

            if (filteredSlots.isEmpty()) {
                response.put("msg", "No Slots found for location: " + location + " and status: " + (status == null ? "ALL" : status));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return new ResponseEntity<>(filteredSlots, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping("/update-slot-status/{slotId}")
    public ResponseEntity<?> updateSlotStatus(@PathVariable Long slotId, @RequestParam String status) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Slot slot = slotService.getSlotById(slotId);
            if (slot == null) {
                response.put("msg", "Slot not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            slot.setStatus(status.toUpperCase()); // Set the new status (ACCEPTED or REJECTED)
            Slot updatedSlot = slotService.saveSlot(slot);

            response.put("msg", "Slot status updated successfully");
            response.put("SlotId", updatedSlot.getId());
            response.put("NewStatus", updatedSlot.getStatus());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("msg", "An error occurred while updating slot status");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }







//    @PostMapping("/add-slot")
//    public ResponseEntity<?> addSlot(@RequestBody Slot slot){
//        HashMap<String, Object> response = new HashMap<>();
//        try {
//            if(slot.getLocation() != null || slot.getTimeSlot() != null ||slot.getEvOwner() != null){
//                response.put("msg", "Loaction,timeSlot are required");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            }
//            Slot addedSlot = slotService.saveSlot(slot);
//            response.put("msg", "slot added successfully");
//            response.put("therapyId", addedSlot.getId());
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        }
//        catch (RuntimeException e) {
//            response.put("msg", e.getMessage());
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
//        } catch (Exception e) {
//            response.put("msg", "An error occurred while adding therapy");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }








//
//    @PostMapping("/add")
//    public ResponseEntity<String> addSlot(@RequestBody Slot slotRequest) {
//        try {
//            Optional<EvOwner> evOwnerOpt = evOwnerRepository.findById(slotRequest.getEvOwner().getId());
//            if (!evOwnerOpt.isPresent()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("EvOwner with this ID does not exist.");
//            }
//
//            EvOwner evOwner = evOwnerOpt.get();
//            Slot newSlot = new Slot();
//            newSlot.setLocation(slotRequest.getLocation());
//            newSlot.setTimeSlot(slotRequest.getTimeSlot());
//            newSlot.setEvOwner(evOwner);
//
//            slotService.saveSlot(newSlot);
//
//            return ResponseEntity.status(HttpStatus.CREATED).body("Slot added successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the slot.");
//        }
//    }




//    @GetMapping("/view-bookings")
//    public ResponseEntity<?> viewBookings() {
//        List<Booking> pendingBookings = bookingService.getPendingBookings();
//
//        if (pendingBookings.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No pending bookings");
//        }
//
//        return ResponseEntity.ok(pendingBookings);
//    }

//    @PostMapping("/accept-booking")
//    public ResponseEntity<?> acceptBooking(@RequestParam Long bookingId) {
//        Booking booking = bookingService.getBookingById(bookingId);
//
//        if (booking == null || !booking.getStatus().equals("PENDING")) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found or already processed");
//        }
//
//        booking.setStatus("ACCEPTED");  // Change the status to "ACCEPTED"
//        bookingService.saveBooking(booking);
//
//        return ResponseEntity.ok("Booking accepted");
//    }
//
//    // 4. EV Owner Rejects a Booking
//    @PostMapping("/reject-booking")
//    public ResponseEntity<?> rejectBooking(@RequestParam Long bookingId) {
//        Booking booking = bookingService.getBookingById(bookingId);
//
//        if (booking == null || !booking.getStatus().equals("PENDING")) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found or already processed");
//        }
//
//        booking.setStatus("REJECTED");  // Change the status to "REJECTED"
//        bookingService.saveBooking(booking);
//
//        return ResponseEntity.ok("Booking rejected");
//    }
//
//
//    @GetMapping("/view-booking-requests")
//    public ResponseEntity<List<Booking>> viewBookingRequests(@RequestParam Long evOwnerId) {
//        try {
//            List<Booking> pendingRequests = bookingService.getPendingRequests(evOwnerId);
//            return ResponseEntity.ok(pendingRequests);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//
//    @PostMapping("/accept-reject-booking")
//    public ResponseEntity<String> acceptRejectBooking(@RequestParam Long bookingRequestId, @RequestParam String action) {
//        try {
//            Booking bookingRequest = bookingService.findBookingRequestById(bookingRequestId);
//
//            if ("ACCEPT".equals(action)) {
//                bookingRequest.setStatus("ACCEPTED");
//            } else if ("REJECT".equals(action)) {
//                bookingRequest.setStatus("REJECTED");
//            } else {
//                return ResponseEntity.badRequest().body("Invalid action.");
//            }
//
//            bookingService.saveBookingRequest(bookingRequest);
//            return ResponseEntity.ok("Booking request " + action + "ed.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
//        }
//    }









}

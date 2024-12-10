package com.ev_centers.project.controller;


import com.ev_centers.project.entity.Booking;
import com.ev_centers.project.entity.EvOwner;
import com.ev_centers.project.entity.Slot;
import com.ev_centers.project.entity.User;
import com.ev_centers.project.service.AdminService;
import com.ev_centers.project.service.BookingService;
import com.ev_centers.project.service.SlotService;
import com.ev_centers.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private SlotService slotService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        HashMap<String, Object> response = new HashMap<>();
        try {
            if (user.getUsername() == null || user.getPassword() == null) {
                response.put("msg", "Username and Password fields are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            User registeredUser = userService.register(user);
            response.put("msg", "Registration successful");
            response.put("userId", registeredUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("msg", "An error occurred during registration");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody HashMap<String, String> loginRequest){
        HashMap<String, Object> response = new HashMap<>();
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            if (username == null || password == null) {
                response.put("msg", "Username and Password fields are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Optional<User> userOptional = userService.login(username, password);
            if (userOptional.isPresent()) {
                response.put("msg", "Login successful");
                response.put("userId", userOptional.get().getId());
                response.put("username",userOptional.get().getUsername());
                return ResponseEntity.ok(response);
            } else {
                response.put("msg", "Invalid username or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("msg", "An error occurred during login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/view-centers")
    public ResponseEntity<?> viewEvCenters(@RequestParam String location) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            List<EvOwner> centers = userService.findAllCenters();

            List<EvOwner> filteredCenters = centers.stream()
                    .filter(center -> center.getLocation().equalsIgnoreCase(location))
                    .collect(Collectors.toList());

            if (filteredCenters.isEmpty()) {
                response.put("msg","No centers found for location: " + location);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return new ResponseEntity<>(filteredCenters, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/slot-request")
    public ResponseEntity<?> bookingSlot(@RequestBody Slot slot) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            slot.setStatus("PENDING"); // Set initial status to PENDING
            Slot addedSlot = slotService.saveSlot(slot);
            response.put("msg", "Slot added successfully");
            response.put("SlotId", addedSlot.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("msg", "An error occurred while adding slot");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/process-payment")
    public ResponseEntity<?> processPayment(
            @RequestParam Long slotId,
            @RequestParam Double amount) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Fetch the slot using Optional
            Optional<Slot> optionalSlot = Optional.ofNullable(slotService.getSlotById(slotId));

            if (!optionalSlot.isPresent()) {
                response.put("msg", "Slot not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Slot slot = optionalSlot.get();

            // Check if the slot is accepted
            if (!"ACCEPTED".equalsIgnoreCase(slot.getStatus())) {
                response.put("msg", "Slot is not accepted yet");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Validate the payment amount
            double requiredAmount = 500.0; // Example required amount for a slot
            if (amount < requiredAmount) {
                response.put("msg", "Insufficient payment. Required: " + requiredAmount);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Simulate dummy payment
            String paymentStatus = "PAID"; // Assume success for simplicity

            // Update the slot's payment status
            slotService.updatePaymentStatus(slotId, paymentStatus);

            response.put("msg", "Payment successful");
            response.put("paymentStatus", paymentStatus);
            response.put("amountPaid", amount);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("msg", "An error occurred while processing payment");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}

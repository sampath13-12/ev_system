package com.ev_centers.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;  // Username of the user who created the request
    private String location;
    private String timeSlot;
    private String status; // PENDING, ACCEPTED, REJECTED

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot; // The slot that is requested
}

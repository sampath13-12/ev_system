package com.ev_centers.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ev_owner")
public class EvOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String username;

    @Column(length = 20)
    private String password;

    @Column(length = 20)
    private String email;

    @Column(length = 20)
    private String location;
}

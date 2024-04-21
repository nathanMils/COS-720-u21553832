package com.project.server.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(
            nullable = false,
            unique = true
    )
    private String token;

    @Column(
            nullable = false
    )
    private Date expiryDate;

    @Column(
            nullable = false
    )
    private boolean revoked;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

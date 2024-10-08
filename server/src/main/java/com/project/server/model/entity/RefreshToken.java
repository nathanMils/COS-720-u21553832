package com.project.server.model.entity;

import com.project.server.converter.TokenConverter;
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
    @Convert(converter = TokenConverter.class)
    private String token;

    @Column(
            nullable = false
    )
    private Date expiryDate;

    @Column(
            nullable = false
    )
    private boolean revoked;

    @Column(
            nullable = false,
            unique = true
    )
    private Long userId;
}

package com.project.server.model.entity;

import com.project.server.converter.TokenConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CONFIRMATION_CODE")
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true
    )
    @Convert(converter = TokenConverter.class)
    private String token;

    @Column(
            nullable = false,
            unique = true
    )
    private Long userId;

    private Date expiryDate;
}

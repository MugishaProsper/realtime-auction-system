package com.webapp.realtimeauctionbackend.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.webapp.realtimeauctionbackend.constants.TransactionStatus;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column
    private String referenceId; // For external references (e.g., auction ID)

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
} 
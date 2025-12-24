package com.tms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shipmentNumber;
    private String origin;
    private String destination;
    private String status;
    private String carrier;
    private Double weight;
    private LocalDateTime createdAt;
}

package com.mirna.busmanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Route {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_id", nullable = false)
    private Location destination;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_id", nullable = false)
    private Location origin;

    @FutureOrPresent(message = "Departure must be in future")
    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;


    @Column(name = "duration", nullable = false)
    private LocalTime duration;

    @Min(value = 1,message = "Minimum cost is 1")
    @Column(name = "cost", nullable = false)
    private int cost;

    public void setDriver(User driver) {
        if (driver.getRole().toString().equals("ROLE_DRIVER")) {
            this.driver = driver;
        } else {
            throw new IllegalArgumentException("User must have ROLE_DRIVER to be assigned as driver.");
        }
    }

}

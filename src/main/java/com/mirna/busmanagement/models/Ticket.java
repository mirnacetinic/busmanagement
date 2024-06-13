package com.mirna.busmanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
public class Ticket {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Min(1)
    @Column(nullable = false)
    private int passengers;

}

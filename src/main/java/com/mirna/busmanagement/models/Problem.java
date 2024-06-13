package com.mirna.busmanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
public class Problem {
    @Id
    @UuidGenerator
    private String id;

    @JoinColumn
    @ManyToOne
    private User user;

    @NotEmpty(message = "This field can't be empty")
    @Column(nullable = false)
    private String description;
}

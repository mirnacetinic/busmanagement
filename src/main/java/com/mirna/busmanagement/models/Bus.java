package com.mirna.busmanagement.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
public class Bus {
    @Id
    @UuidGenerator
    @Column
    private String id;

    @NotEmpty(message = "Field can't be empty!")
    @Length(min = 7, max = 8, message = "Registration must be either 7 or 8 characters long!")
    @Column(unique = true, nullable = false)
    private String registration;

    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private int capacity;
}

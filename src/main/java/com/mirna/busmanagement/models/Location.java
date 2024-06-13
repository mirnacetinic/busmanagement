package com.mirna.busmanagement.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
public class Location {
    @Id
    @UuidGenerator
    @Column
    private String id;

    @NotEmpty(message = "This field should be empty!")
    @Length(min = 2, max = 20, message = "Wrong format")
    @Column(nullable = false)
    private String city;

    @NotEmpty(message = "This field should be empty!")
    @Length(min = 2, max = 20, message = "Wrong format")
    @Column(nullable = false)
    private String country;

    @Length(min = 5, max = 5, message = "ZIP code must be 5 characters long!")
    @Column(unique = true, nullable = false)
    private String ZIP;
}

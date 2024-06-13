package com.mirna.busmanagement.dtos;

import com.mirna.busmanagement.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDto {

    @Length(min = 2,max = 30,message = "Name must be 2-30 characters long")
    @NotEmpty(message = "Name shouldn't be empty")
    private String name;

    @Length(min = 2,max = 30,message = "Surname must be 2-30 characters long")
    @NotEmpty(message = "Surname shouldn't be empty")
    private String surname;

    @Length(min = 2,max = 30,message = "Username must be 2-30 characters long")
    @NotEmpty(message = "Username shouldn't be empty")
    private String username;

    @Email(message = "Wrong format for email")
    @NotEmpty(message = "Email shouldn't be empty")
    private String email;

    @NotEmpty(message = "Password shouldn't be empty")
    private String password;

    private Role role=Role.ROLE_PASSENGER;
}

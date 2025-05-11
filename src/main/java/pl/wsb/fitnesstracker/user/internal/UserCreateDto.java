package pl.wsb.fitnesstracker.user.internal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO for creating a new user.
 *
 * @param firstName The first name of the user
 * @param lastName  The last name of the user
 * @param birthdate The birth date of the user
 * @param email     The email address of the user
 */
public record UserCreateDto(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @Past(message = "Birth date must be in the past") @NotNull(message = "Birth date is required") LocalDate birthdate,
        @Email(message = "Invalid email format") @NotBlank(message = "Email is required") String email
) {}
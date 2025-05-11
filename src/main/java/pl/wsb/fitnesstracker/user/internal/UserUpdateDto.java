package pl.wsb.fitnesstracker.user.internal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

/**
 * DTO for updating user information.
 *
 * @param firstName The first name of the user (optional)
 * @param lastName  The last name of the user (optional)
 * @param birthdate The birth date of the user (optional)
 * @param email     The email address of the user (optional)
 */
public record UserUpdateDto(String firstName, String lastName, @Past LocalDate birthdate, @Email String email) {}
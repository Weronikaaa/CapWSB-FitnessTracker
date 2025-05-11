package pl.wsb.fitnesstracker.user.internal;

import java.time.LocalDate;

/**
 * DTO for detailed user information.
 *
 * @param id         The unique identifier of the user
 * @param firstName  The first name of the user
 * @param lastName   The last name of the user
 * @param birthdate  The birth date of the user
 * @param email      The email address of the user
 */
public record UserDetailsDto(Long id, String firstName, String lastName, LocalDate birthdate, String email) {}
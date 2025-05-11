package pl.wsb.fitnesstracker.user.internal;

/**
 * DTO for user information when searching by email.
 *
 * @param id    The unique identifier of the user
 * @param email The email address of the user
 */
public record UserEmailDto(Long id, String email) {}
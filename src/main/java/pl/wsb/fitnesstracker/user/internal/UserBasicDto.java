package pl.wsb.fitnesstracker.user.internal;

/**
 * DTO for listing basic user information.
 *
 * @param id   The unique identifier of the user
 * @param name The full name of the user (firstName + lastName)
 */
public record UserBasicDto(Long id, String name) {}
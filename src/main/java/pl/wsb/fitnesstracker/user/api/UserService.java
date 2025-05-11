package pl.wsb.fitnesstracker.user.api;

import pl.wsb.fitnesstracker.user.internal.*;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing user-related operations.
 */
public interface UserService {

    /**
     * Lists all users with basic information.
     *
     * @return List of UserBasicDto
     */
    List<UserBasicDto> listUsers();

    /**
     * Retrieves details of a user by ID.
     *
     * @param id The user ID
     * @return Optional containing UserDetailsDto
     */
    Optional<UserDetailsDto> getUserDetails(Long id);

    /**
     * Creates a new user.
     *
     * @param createDto The user creation DTO
     * @return Created UserDetailsDto
     */
    UserDetailsDto createUser(UserCreateDto createDto);

    /**
     * Deletes a user by ID.
     *
     * @param id The user ID
     * @return True if deleted, false if not found
     */
    boolean deleteUser(Long id);

    /**
     * Searches users by email fragment (case-insensitive).
     *
     * @param emailFragment The email fragment to search for
     * @return List of UserEmailDto
     */
    List<UserEmailDto> searchByEmail(String emailFragment);

    /**
     * Searches users older than the specified age.
     *
     * @param age The age threshold
     * @return List of UserDetailsDto
     */
    List<UserDetailsDto> searchByAgeGreaterThan(int age);

    /**
     * Updates a user's information.
     *
     * @param id        The user ID
     * @param updateDto The user update DTO
     * @return Optional containing updated UserDetailsDto
     */
    Optional<UserDetailsDto> updateUser(Long id, UserUpdateDto updateDto);
}
package pl.wsb.fitnesstracker.user.internal;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.UserService;

import java.util.List;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/v1/users")
class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Lists all users with basic information.
     *
     * @return List of UserBasicDto
     */
    @GetMapping
    public List<UserBasicDto> listUsers() {
        return userService.listUsers();
    }

    /**
     * Retrieves details of a user by ID.
     *
     * @param id The user ID
     * @return UserDetailsDto or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable Long id) {
        return userService.getUserDetails(id)
                .map(userDetailsDto -> ResponseEntity.ok(userDetailsDto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new user.
     *
     * @param createDto The user creation DTO
     * @return Created UserDetailsDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailsDto createUser(@Valid @RequestBody UserCreateDto createDto) {
        return userService.createUser(createDto);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The user ID
     * @return 204 if deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Searches users by email fragment.
     *
     * @param email The email fragment to search for
     * @return List of UserEmailDto
     */
    @GetMapping("/search/email")
    public List<UserEmailDto> searchByEmail(@RequestParam String email) {
        return userService.searchByEmail(email);
    }

    /**
     * Searches users older than the specified age.
     *
     * @param age The age threshold
     * @return List of UserDetailsDto
     */
    @GetMapping("/search/age")
    public List<UserDetailsDto> searchByAgeGreaterThan(@RequestParam int age) {
        return userService.searchByAgeGreaterThan(age);
    }

    /**
     * Updates a user's information.
     *
     * @param id        The user ID
     * @param updateDto The user update DTO
     * @return Updated UserDetailsDto or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto updateDto) {
        return userService.updateUser(id, updateDto)
                .map(userDetailsDto -> ResponseEntity.ok(userDetailsDto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
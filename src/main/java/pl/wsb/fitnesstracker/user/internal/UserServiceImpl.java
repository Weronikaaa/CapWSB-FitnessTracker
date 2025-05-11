package pl.wsb.fitnesstracker.user.internal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserService;
import pl.wsb.fitnesstracker.training.internal.TrainingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of UserService for managing user-related operations.
 */
@Service
@Transactional
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;

    public UserServiceImpl(UserRepository userRepository, TrainingRepository trainingRepository) {
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
    }

    @Override
    public List<UserBasicDto> listUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserBasicDto(user.getId(), user.getFirstName() + " " + user.getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDetailsDto> getUserDetails(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDetailsDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getBirthdate(),
                        user.getEmail()
                ));
    }

    @Override
    public UserDetailsDto createUser(UserCreateDto createDto) {
        // Sprawdzenie unikalności e-maila
        if (userRepository.findByEmailContainingIgnoreCase(createDto.email()).stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(createDto.email()))) {
            throw new IllegalArgumentException("Email " + createDto.email() + " is already in use");
        }

        User user = new User(
                createDto.firstName(),
                createDto.lastName(),
                createDto.birthdate(),
                createDto.email()
        );
        user = userRepository.save(user);
        return new UserDetailsDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthdate(),
                user.getEmail()
        );
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            // Usuwamy powiązane treningi przed usunięciem użytkownika
            trainingRepository.deleteByUserId(id);
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<UserEmailDto> searchByEmail(String emailFragment) {
        return userRepository.findByEmailContainingIgnoreCase(emailFragment).stream()
                .map(user -> new UserEmailDto(user.getId(), user.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDetailsDto> searchByAgeGreaterThan(int age) {
        LocalDate thresholdDate = LocalDate.now().minusYears(age);
        return userRepository.findByBirthdateBefore(thresholdDate).stream()
                .map(user -> new UserDetailsDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getBirthdate(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDetailsDto> updateUser(Long id, UserUpdateDto updateDto) {
        return userRepository.findById(id).map(user -> {
            // Sprawdzenie unikalności e-maila, jeśli jest aktualizowany
            if (updateDto.email() != null && !updateDto.email().equalsIgnoreCase(user.getEmail())) {
                if (userRepository.findByEmailContainingIgnoreCase(updateDto.email()).stream()
                        .anyMatch(u -> u.getEmail().equalsIgnoreCase(updateDto.email()))) {
                    throw new IllegalArgumentException("Email " + updateDto.email() + " is already in use");
                }
            }

            // Aktualizujemy istniejący rekord za pomocą setterów
            if (updateDto.firstName() != null) {
                user.setFirstName(updateDto.firstName());
            }
            if (updateDto.lastName() != null) {
                user.setLastName(updateDto.lastName());
            }
            if (updateDto.birthdate() != null) {
                user.setBirthdate(updateDto.birthdate());
            }
            if (updateDto.email() != null) {
                user.setEmail(updateDto.email());
            }

            // Zapisujemy zaktualizowany rekord
            User updatedUser = userRepository.save(user);
            return new UserDetailsDto(
                    updatedUser.getId(),
                    updatedUser.getFirstName(),
                    updatedUser.getLastName(),
                    updatedUser.getBirthdate(),
                    updatedUser.getEmail()
            );
        });
    }
}
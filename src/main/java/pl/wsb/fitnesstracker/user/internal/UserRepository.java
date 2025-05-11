package pl.wsb.fitnesstracker.user.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds users whose email contains the given fragment (case-insensitive).
     *
     * @param emailFragment The fragment of the email to search for
     * @return List of users matching the email fragment
     */
    List<User> findByEmailContainingIgnoreCase(String emailFragment);

    /**
     * Finds users born before the specified date (i.e., older than a certain age).
     *
     * @param birthdate The date threshold (users born before this date)
     * @return List of users older than the specified age
     */
    List<User> findByBirthdateBefore(LocalDate birthdate);
}
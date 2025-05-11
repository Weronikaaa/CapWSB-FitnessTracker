package pl.wsb.fitnesstracker.training.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wsb.fitnesstracker.training.api.Training;

/**
 * Repository for managing Training entities.
 */
public interface TrainingRepository extends JpaRepository<Training, Long> {

    /**
     * Deletes all trainings associated with the given user ID.
     *
     * @param userId The ID of the user
     */
    void deleteByUserId(Long userId);
}
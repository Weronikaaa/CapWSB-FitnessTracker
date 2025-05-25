package pl.wsb.fitnesstracker.training.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wsb.fitnesstracker.training.api.Training;

import java.util.Date;
import java.util.List;

/**
 * Repository for managing Training entities.
 */
public interface TrainingRepository extends JpaRepository<Training, Long> {

    /**
     * Deletes all trainings associated with the given user ID.
     *
     * @param userId the ID of the user
     */
    void deleteByUserId(Long userId);

    /**
     * Finds all trainings for a specific user.
     *
     * @param userId the ID of the user
     * @return list of trainings
     */
    List<Training> findByUserId(Long userId);

    /**
     * Finds all trainings that ended after the specified date.
     *
     * @param date the date to compare
     * @return list of trainings
     */
    List<Training> findByEndTimeAfter(Date date);

    /**
     * Finds all trainings of a specific activity type.
     *
     * @param activityType the activity type
     * @return list of trainings
     */
    List<Training> findByActivityType(ActivityType activityType);
}
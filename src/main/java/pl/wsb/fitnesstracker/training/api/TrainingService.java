package pl.wsb.fitnesstracker.training.api;

import pl.wsb.fitnesstracker.training.internal.ActivityType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing training operations.
 */
public interface TrainingService {

    /**
     * Lists all trainings.
     *
     * @return list of training details
     */
    List<TrainingDetailsDto> listTrainings();

    /**
     * Gets details of a specific training.
     *
     * @param id the training ID
     * @return optional training details
     */
    Optional<TrainingDetailsDto> getTraining(Long id);

    /**
     * Creates a new training for a user.
     *
     * @param userId    the ID of the user
     * @param createDto the training creation data
     * @return created training details
     */
    TrainingDetailsDto createTraining(Long userId, TrainingCreateDto createDto);

    /**
     * Updates an existing training.
     *
     * @param id        the training ID
     * @param updateDto the training update data
     * @return optional updated training details
     */
    Optional<TrainingDetailsDto> updateTraining(Long id, TrainingUpdateDto updateDto);

    /**
     * Deletes a training.
     *
     * @param id the training ID
     * @return true if deleted, false otherwise
     */
    boolean deleteTraining(Long id);

    /**
     * Finds trainings for a specific user.
     *
     * @param userId the ID of the user
     * @return list of training details
     */
    List<TrainingDetailsDto> findTrainingsByUser(Long userId);

    /**
     * Finds trainings that ended after the specified date.
     *
     * @param date the date to compare
     * @return list of training details
     */
    List<TrainingDetailsDto> findTrainingsEndedAfter(Date date);

    /**
     * Finds trainings of a specific activity type.
     *
     * @param activityType the activity type
     * @return list of training details
     */
    List<TrainingDetailsDto> findTrainingsByActivityType(ActivityType activityType);
}
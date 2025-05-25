package pl.wsb.fitnesstracker.training.internal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.training.api.*;
import pl.wsb.fitnesstracker.training.internal.ActivityType;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Controller for handling training-related HTTP requests.
 */
@RestController
@RequestMapping("/v1/trainings")
class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    /**
     * Lists all trainings.
     *
     * @return list of training details
     */
    @GetMapping
    public List<TrainingDetailsDto> listTrainings() {
        return trainingService.listTrainings();
    }

    /**
     * Gets details of a specific training.
     *
     * @param id the training ID
     * @return response with training details or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainingDetailsDto> getTraining(@PathVariable Long id) {
        return trainingService.getTraining(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new training for a user.
     *
     * @param userId    the ID of the user
     * @param createDto the training creation data
     * @return created training details
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingDetailsDto createTraining(@RequestParam Long userId, @Valid @RequestBody TrainingCreateDto createDto) {
        return trainingService.createTraining(userId, createDto);
    }

    /**
     * Updates an existing training.
     *
     * @param id        the training ID
     * @param updateDto the training update data
     * @return response with updated training details or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrainingDetailsDto> updateTraining(@PathVariable Long id, @Valid @RequestBody TrainingUpdateDto updateDto) {
        return trainingService.updateTraining(id, updateDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a training.
     *
     * @param id the training ID
     * @return 204 if deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long id) {
        boolean deleted = trainingService.deleteTraining(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Finds trainings for a specific user.
     *
     * @param userId the ID of the user
     * @return list of training details
     */
    @GetMapping("/user/{userId}")
    public List<TrainingDetailsDto> findTrainingsByUser(@PathVariable Long userId) {
        return trainingService.findTrainingsByUser(userId);
    }

    /**
     * Finds trainings that ended after the specified date.
     *
     * @param date the date to compare
     * @return list of training details
     */
    @GetMapping("/ended-after")
    public List<TrainingDetailsDto> findTrainingsEndedAfter(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date date) {
        return trainingService.findTrainingsEndedAfter(date);
    }

    /**
     * Finds trainings of a specific activity type.
     *
     * @param activityType the activity type
     * @return list of training details
     */
    @GetMapping("/activity")
    public List<TrainingDetailsDto> findTrainingsByActivityType(@RequestParam ActivityType activityType) {
        return trainingService.findTrainingsByActivityType(activityType);
    }
}
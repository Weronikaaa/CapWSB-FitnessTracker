package pl.wsb.fitnesstracker.training.internal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wsb.fitnesstracker.training.api.*;
import pl.wsb.fitnesstracker.training.internal.ActivityType;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.internal.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of TrainingService for managing training operations.
 */
@Service
@Transactional
class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository, UserRepository userRepository) {
        this.trainingRepository = trainingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TrainingDetailsDto> listTrainings() {
        return trainingRepository.findAll().stream()
                .map(this::toDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TrainingDetailsDto> getTraining(Long id) {
        return trainingRepository.findById(id)
                .map(this::toDetailsDto);
    }

    @Override
    public TrainingDetailsDto createTraining(Long userId, TrainingCreateDto createDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

        Training training = new Training(
                user,
                createDto.getStartTime(),
                createDto.getEndTime(),
                createDto.getActivityType(),
                createDto.getDistance(),
                createDto.getAverageSpeed()
        );
        training = trainingRepository.save(training);
        return toDetailsDto(training);
    }

    @Override
    public Optional<TrainingDetailsDto> updateTraining(Long id, TrainingUpdateDto updateDto) {
        return trainingRepository.findById(id).map(training -> {
            if (updateDto.getDistance() != null) {
                training.setDistance(updateDto.getDistance());
            }
            if (updateDto.getAverageSpeed() != null) {
                training.setAverageSpeed(updateDto.getAverageSpeed());
            }
            if (updateDto.getStartTime() != null) {
                training.setStartTime(updateDto.getStartTime());
            }
            if (updateDto.getEndTime() != null) {
                training.setEndTime(updateDto.getEndTime());
            }
            if (updateDto.getActivityType() != null) {
                training.setActivityType(updateDto.getActivityType());
            }
            Training updatedTraining = trainingRepository.save(training);
            return toDetailsDto(updatedTraining);
        });
    }

    @Override
    public boolean deleteTraining(Long id) {
        if (trainingRepository.existsById(id)) {
            trainingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<TrainingDetailsDto> findTrainingsByUser(Long userId) {
        return trainingRepository.findByUserId(userId).stream()
                .map(this::toDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingDetailsDto> findTrainingsEndedAfter(Date date) {
        return trainingRepository.findByEndTimeAfter(date).stream()
                .map(this::toDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingDetailsDto> findTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType).stream()
                .map(this::toDetailsDto)
                .collect(Collectors.toList());
    }

    private TrainingDetailsDto toDetailsDto(Training training) {
        return new TrainingDetailsDto(
                training.getId(),
                training.getUser().getId(),
                training.getStartTime(),
                training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed()
        );
    }
}
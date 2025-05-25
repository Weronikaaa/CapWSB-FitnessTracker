package pl.wsb.fitnesstracker.training.api;

import pl.wsb.fitnesstracker.training.internal.ActivityType;

import java.util.Date;

/**
 * DTO for retrieving training details.
 */
public class TrainingDetailsDto {

    private final Long id;
    private final Long userId;
    private final Date startTime;
    private final Date endTime;
    private final ActivityType activityType;
    private final double distance;
    private final double averageSpeed;

    /**
     * Constructs a new TrainingDetailsDto.
     *
     * @param id           the training ID
     * @param userId       the ID of the associated user
     * @param startTime    the start time of the training
     * @param endTime      the end time of the training
     * @param activityType the type of activity
     * @param distance     the distance covered
     * @param averageSpeed the average speed
     */
    public TrainingDetailsDto(Long id, Long userId, Date startTime, Date endTime, ActivityType activityType, double distance, double averageSpeed) {
        this.id = id;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.activityType = activityType;
        this.distance = distance;
        this.averageSpeed = averageSpeed;
    }

    // Gettery
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public double getDistance() {
        return distance;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }
}
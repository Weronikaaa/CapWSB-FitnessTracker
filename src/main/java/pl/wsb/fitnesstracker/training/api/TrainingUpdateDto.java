package pl.wsb.fitnesstracker.training.api;

import pl.wsb.fitnesstracker.training.internal.ActivityType;

import java.util.Date;

/**
 * DTO for updating a training.
 */
public class TrainingUpdateDto {

    private final Double distance;
    private final Double averageSpeed;
    private final Date startTime;
    private final Date endTime;
    private final ActivityType activityType;

    /**
     * Constructs a new TrainingUpdateDto.
     *
     * @param distance     the updated distance (nullable)
     * @param averageSpeed the updated average speed (nullable)
     * @param startTime    the updated start time (nullable)
     * @param endTime      the updated end time (nullable)
     * @param activityType the updated activity type (nullable)
     */
    public TrainingUpdateDto(Double distance, Double averageSpeed, Date startTime, Date endTime, ActivityType activityType) {
        this.distance = distance;
        this.averageSpeed = averageSpeed;
        this.startTime = startTime;
        this.endTime = endTime;
        this.activityType = activityType;
    }

    // Gettery
    public Double getDistance() {
        return distance;
    }

    public Double getAverageSpeed() {
        return averageSpeed;
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
}
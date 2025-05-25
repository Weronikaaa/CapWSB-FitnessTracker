package pl.wsb.fitnesstracker.training.api;

import jakarta.validation.constraints.NotNull;
import pl.wsb.fitnesstracker.training.internal.ActivityType;

import java.util.Date;

/**
 * DTO for creating a new training.
 */
public class TrainingCreateDto {

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    private ActivityType activityType;

    private double distance;

    private double averageSpeed;

    /**
     * Constructs a new TrainingCreateDto.
     *
     * @param startTime    the start time of the training
     * @param endTime      the end time of the training
     * @param activityType the type of activity
     * @param distance     the distance covered
     * @param averageSpeed the average speed
     */
    public TrainingCreateDto(Date startTime, Date endTime, ActivityType activityType, double distance, double averageSpeed) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.activityType = activityType;
        this.distance = distance;
        this.averageSpeed = averageSpeed;
    }

    // Gettery
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
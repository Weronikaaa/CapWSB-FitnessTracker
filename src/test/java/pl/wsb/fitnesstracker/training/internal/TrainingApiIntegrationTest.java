package pl.wsb.fitnesstracker.training.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.wsb.fitnesstracker.training.api.TrainingCreateDto;
import pl.wsb.fitnesstracker.training.api.TrainingUpdateDto;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.internal.UserRepository;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Training API endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("loadInitialData")
class TrainingApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    private Long userId;
    private Date startTime;
    private Date endTime;

    @BeforeEach
    void setUp() throws Exception {
        trainingRepository.deleteAll();
        userRepository.deleteAll();
        User user = new User("Emma", "Johnson", java.time.LocalDate.of(1997, 5, 11), "emma.johnson@domain.com");
        User savedUser = userRepository.save(user);
        userId = savedUser.getId();
        startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2025-05-25 10:00:00");
        endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2025-05-25 11:00:00");
    }

    @Test
    void shouldListTrainings() throws Exception {
        mockMvc.perform(get("/v1/trainings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldCreateTraining() throws Exception {
        TrainingCreateDto createDto = new TrainingCreateDto(startTime, endTime, ActivityType.RUNNING, 5.0, 10.0);
        mockMvc.perform(post("/v1/trainings?userId=" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.activityType").value("RUNNING"))
                .andExpect(jsonPath("$.distance").value(5.0));
    }

    @Test
    void shouldGetTraining() throws Exception {
        TrainingCreateDto createDto = new TrainingCreateDto(startTime, endTime, ActivityType.CYCLING, 10.0, 20.0);
        String response = mockMvc.perform(post("/v1/trainings?userId=" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long trainingId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/v1/trainings/" + trainingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(trainingId))
                .andExpect(jsonPath("$.activityType").value("CYCLING"))
                .andExpect(jsonPath("$.distance").value(10.0));
    }

    @Test
    void shouldUpdateTraining() throws Exception {
        TrainingCreateDto createDto = new TrainingCreateDto(startTime, endTime, ActivityType.SWIMMING, 3.0, 5.0);
        String response = mockMvc.perform(post("/v1/trainings?userId=" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long trainingId = objectMapper.readTree(response).get("id").asLong();

        TrainingUpdateDto updateDto = new TrainingUpdateDto(4.0, null, null, null, null);
        mockMvc.perform(put("/v1/trainings/" + trainingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(trainingId))
                .andExpect(jsonPath("$.distance").value(4.0));
    }

    @Test
    void shouldDeleteTraining() throws Exception {
        TrainingCreateDto createDto = new TrainingCreateDto(startTime, endTime, ActivityType.WALKING, 0.0, 0.0);
        String response = mockMvc.perform(post("/v1/trainings?userId=" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long trainingId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/v1/trainings/" + trainingId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldFindTrainingsByUser() throws Exception {
        TrainingCreateDto createDto = new TrainingCreateDto(startTime, endTime, ActivityType.RUNNING, 5.0, 10.0);
        mockMvc.perform(post("/v1/trainings?userId=" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/v1/trainings/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].activityType").value("RUNNING"));
    }

    @Test
    void shouldFindTrainingsEndedAfter() throws Exception {
        TrainingCreateDto createDto = new TrainingCreateDto(startTime, endTime, ActivityType.CYCLING, 10.0, 20.0);
        mockMvc.perform(post("/v1/trainings?userId=" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated());

        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2025-05-25 09:00:00");
        String formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .format(Instant.ofEpochMilli(date.getTime()).atZone(java.time.ZoneOffset.UTC));
        mockMvc.perform(get("/v1/trainings/ended-after?date=" + formattedDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].activityType").value("CYCLING"));
    }

    @Test
    void shouldFindTrainingsByActivityType() throws Exception {
        TrainingCreateDto createDto = new TrainingCreateDto(startTime, endTime, ActivityType.SWIMMING, 3.0, 5.0);
        mockMvc.perform(post("/v1/trainings?userId=" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/v1/trainings/activity?activityType=SWIMMING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].activityType").value("SWIMMING"));
    }
}
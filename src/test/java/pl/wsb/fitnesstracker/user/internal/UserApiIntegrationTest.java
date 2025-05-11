package pl.wsb.fitnesstracker.user.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.wsb.fitnesstracker.training.internal.TrainingRepository;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("loadInitialData")
class UserApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    private Long userId; // Zmienna do przechowywania ID użytkownika

    @BeforeEach
    void setUp() {
        // Czyszczenie powiązanych treningów
        trainingRepository.deleteAll();
        // Czyszczenie użytkowników
        userRepository.deleteAll();
        // Dodanie użytkownika testowego
        User user = new User("Emma", "Johnson", LocalDate.of(1997, 5, 11), "emma.johnson@domain.com");
        User savedUser = userRepository.save(user);
        userId = savedUser.getId(); // Zapisanie ID użytkownika
        System.out.println("Saved user: " + savedUser); // Logowanie dla debugowania
    }

    @Test
    void shouldListUsers() throws Exception {
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void shouldGetUserDetails() throws Exception {
        mockMvc.perform(get("/v1/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.birthdate").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserCreateDto createDto = new UserCreateDto(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "john.doe.unique@example.com"
        );
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthdate").value("1990-01-01"))
                .andExpect(jsonPath("$.email").value("john.doe.unique@example.com"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/v1/users/" + userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldSearchByEmail() throws Exception {
        mockMvc.perform(get("/v1/users/search/email").param("email", "johnson"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email").value("emma.johnson@domain.com"));
    }

    @Test
    void shouldSearchByAge() throws Exception {
        mockMvc.perform(get("/v1/users/search/age").param("age", "25"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].firstName").exists());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserUpdateDto updateDto = new UserUpdateDto(null, null, null, "new.email@example.com");
        mockMvc.perform(put("/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("new.email@example.com"));
    }
}
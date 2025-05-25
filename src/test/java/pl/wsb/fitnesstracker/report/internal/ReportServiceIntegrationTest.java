package pl.wsb.fitnesstracker.report.internal;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;
import pl.wsb.FitnessTracker;
import pl.wsb.fitnesstracker.report.ReportService;
import pl.wsb.fitnesstracker.report.internal.ReportServiceImpl;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.internal.ActivityType;
import pl.wsb.fitnesstracker.training.internal.TrainingRepository;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.internal.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for ReportService.
 */
@SpringBootTest(classes = FitnessTracker.class)
@ActiveProfiles("loadInitialData")
class ReportServiceIntegrationTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    private GreenMail greenMail;

    @Configuration
    static class TestConfig {
        @Bean
        public JavaMailSender javaMailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("localhost");
            mailSender.setPort(2525);
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.smtp.auth", "false");
            props.put("mail.smtp.starttls.enable", "false");
            return mailSender;
        }

        @Bean
        public ReportService reportService(UserRepository userRepository, TrainingRepository trainingRepository, JavaMailSender mailSender) {
            return new ReportServiceImpl(userRepository, trainingRepository, mailSender);
        }
    }

    @BeforeEach
    void setUp() {
        ServerSetup setup = new ServerSetup(2525, "localhost", "smtp");
        greenMail = new GreenMail(setup);
        greenMail.start();
        trainingRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
    }

    @Test
    void shouldSendMonthlyReport() throws Exception {
        // Przygotowanie danych
        User user = new User("Emma", "Johnson", java.time.LocalDate.of(1997, 5, 11), "emma.johnson@domain.com");
        user = userRepository.save(user);
        Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2025-05-01 10:00:00");
        Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2025-05-01 11:00:00");
        Training training = new Training(user, startTime, endTime, ActivityType.RUNNING, 5.0, 10.0);
        trainingRepository.save(training);

        // Wywołanie raportu
        reportService.generateAndSendMonthlyReports();

        // Weryfikacja e-maila
        assertEquals(1, greenMail.getReceivedMessages().length);
        assertEquals("emma.johnson@domain.com", greenMail.getReceivedMessages()[0].getAllRecipients()[0].toString());
        assertEquals("Monthly Training Report", greenMail.getReceivedMessages()[0].getSubject());

        // Normalizacja treści e-maila
        String expected = "Hello Emma,\n\nYou have completed 1 training(s) this month.\n\nBest regards,\nFitnessTracker Team";
        String actual = GreenMailUtil.getBody(greenMail.getReceivedMessages()[0])
                .replaceAll("\\r\\n|\\r|\\n", "\n") // Ujednolicenie znaków nowej linii
                .replaceAll("\\s+$", "") // Usunięcie końcowych białych znaków
                .trim();
        assertEquals(expected, actual);
    }
}
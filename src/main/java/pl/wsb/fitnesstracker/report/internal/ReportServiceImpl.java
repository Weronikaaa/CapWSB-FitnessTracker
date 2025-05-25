package pl.wsb.fitnesstracker.report.internal;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.report.ReportService;
import pl.wsb.fitnesstracker.training.internal.TrainingRepository;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.internal.UserRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Implementation of ReportService for generating and sending training reports.
 */
@Service
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;
    private final JavaMailSender mailSender;

    public ReportServiceImpl(UserRepository userRepository, TrainingRepository trainingRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
        this.mailSender = mailSender;
    }

    @Override
    public void generateAndSendMonthlyReports() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfMonth = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        Date endOfMonth = calendar.getTime();

        List<User> users = userRepository.findAll();
        for (User user : users) {
            long trainingCount = trainingRepository.findByUserId(user.getId()).stream()
                    .filter(training -> !training.getEndTime().before(startOfMonth) && training.getEndTime().before(endOfMonth))
                    .count();

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Monthly Training Report");
            message.setText(String.format("Hello %s,\n\nYou have completed %d training(s) this month.\n\nBest regards,\nFitnessTracker Team",
                    user.getFirstName(), trainingCount));
            mailSender.send(message);
        }
    }
}
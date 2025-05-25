package pl.wsb.fitnesstracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pl.wsb.fitnesstracker.report.ReportService;

/**
 * Configuration for scheduling tasks.
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

    private final ReportService reportService;

    public SchedulingConfig(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Schedules monthly report generation on the first day of each month.
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void scheduleMonthlyReports() {
        reportService.generateAndSendMonthlyReports();
    }
}
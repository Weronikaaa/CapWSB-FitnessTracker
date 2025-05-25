package pl.wsb.fitnesstracker.report;

/**
 * Service interface for generating and sending training reports.
 */
public interface ReportService {

    /**
     * Generates and sends monthly training reports for all users.
     */
    void generateAndSendMonthlyReports();
}
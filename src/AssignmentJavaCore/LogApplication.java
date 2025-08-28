package AssignmentJavaCore;

import AssignmentJavaCore.config.AppConfig;
import AssignmentJavaCore.core.LogProcessor;
import AssignmentJavaCore.exporters.LogExporter;
import AssignmentJavaCore.exporters.TextLogExporter;
import AssignmentJavaCore.filters.*;
import AssignmentJavaCore.model.SearchCriteria;
import AssignmentJavaCore.parsers.LogParser;
import AssignmentJavaCore.model.SearchResult;
import AssignmentJavaCore.parsers.DefaultLogParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


public class LogApplication {

    private static final String OUTPUT_DIR = "src/AssignmentJavaCore/output/";
    private LogProcessor processor;
    private LogExporter exporter;
    private LogParser parser;
    private Scanner scanner;

    public LogApplication() {
        this.parser = new DefaultLogParser();
        this.processor = new LogProcessor(parser);
        this.exporter = new TextLogExporter();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        LogApplication app = new LogApplication();
        try {
            app.run();
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
        } finally {
            app.cleanup();
        }
    }

    public void run() throws IOException, ExecutionException, InterruptedException {
        System.out.println("=== Log Search Application ===");

        while (true) {
            displayMenu();
            int choice = getChoice();

            switch (choice) {
                case 1 -> searchByLevel();
                case 2 -> searchByTimeRange();
                case 3 -> searchByMessage();
                case 4 -> searchByCombinedCriteria();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private void displayMenu() {
        System.out.println("\n=== Search Options ===");
        System.out.println("1. Search by Log Level (INFO, WARN, ERROR, DEBUG)");
        System.out.println("2. Search by Time Range");
        System.out.println("3. Search by Message Content");
        System.out.println("4. Search by Combined Criteria");
        System.out.println("0. Exit");
    }

    private int getChoice() {
        while (true) {
            try {
                System.out.print("Enter your choice: ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    continue; // Bỏ qua input rỗng
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String getInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        return input != null ? input.trim() : "";
    }

    private void searchByLevel() throws IOException, ExecutionException, InterruptedException {
        String level = getInput("Enter log level (INFO, WARN, ERROR, DEBUG): ");
        System.out.println("Searching for log level: " + level);
        if (level.isEmpty()) {
            System.out.println("Log level cannot be empty.");
            return;
        }

        SearchCriteria criteria = new SearchCriteria();
        criteria.setLevel(level);
        LogFilter filter = new DefaultLogFilter(criteria);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String outputFileName = "search_by_level_" + level.toLowerCase() + timestamp + ".txt";
        String fullOutputPath = OUTPUT_DIR + outputFileName;

        SearchResult result = processor.process(AppConfig.DEFAULT_LOG_PATH, filter, fullOutputPath);
        processResults(result, filter.getDescription(), "search_by_level_" + level.toLowerCase());
    }

    private void searchByTimeRange() throws IOException, ExecutionException, InterruptedException {
        System.out.println("Enter time range (format: yyyy-MM-dd HH:mm:ss)");
        String startTimeStr = getInput("Start time (press Enter to skip): ");
        String endTimeStr = getInput("End time (press Enter to skip): ");

        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        if (!startTimeStr.isEmpty()) {
            Optional<LocalDateTime> parsed = parseTimestamp(startTimeStr);
            if (parsed.isEmpty()) {
                System.out.println("Invalid start time format. Use: yyyy-MM-dd HH:mm:ss");
                return;
            }
            startTime = parsed.get();
        }

        if (!endTimeStr.isEmpty()) {
            Optional<LocalDateTime> parsed = parseTimestamp(endTimeStr);
            if (parsed.isEmpty()) {
                System.out.println("Invalid end time format. Use: yyyy-MM-dd HH:mm:ss");
                return;
            }
            endTime = parsed.get();
        }

        if (startTime == null && endTime == null) {
            System.out.println("At least one time boundary must be specified.");
            return;
        }

        SearchCriteria criteria = new SearchCriteria();
        criteria.setStartTime(startTime);
        criteria.setEndTime(endTime);
        LogFilter filter = new DefaultLogFilter(criteria);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String outputFileName = "search_by_time_range_" + timestamp + ".txt";
        String fullOutputPath = OUTPUT_DIR + outputFileName;

        SearchResult result = processor.process(AppConfig.DEFAULT_LOG_PATH, filter, fullOutputPath);
        processResults(result, filter.getDescription(), "search_by_time_range");
    }

    private void searchByMessage() throws IOException, ExecutionException, InterruptedException {
        String keyword = getInput("Enter keyword to search in messages: ");

        if (keyword.isEmpty()) {
            System.out.println("Keyword cannot be empty.");
            return;
        }

        SearchCriteria criteria = new SearchCriteria();
        criteria.setKeyword(keyword);
        LogFilter filter = new DefaultLogFilter(criteria);
        String outputFileName = "search_by_message_" + sanitizeFilename(keyword) + ".txt";
        String fullOutputPath = OUTPUT_DIR + outputFileName;

        SearchResult result = processor.process(AppConfig.DEFAULT_LOG_PATH, filter, fullOutputPath);
        processResults(result, filter.getDescription(), "search_by_message_" + sanitizeFilename(keyword));
    }

    private void searchByCombinedCriteria() throws IOException, ExecutionException, InterruptedException {
        System.out.println("=== Combined Search ===");
        System.out.println("Leave any field empty to skip that filter");
        String level = getInput("Log level (INFO, WARN, ERROR, DEBUG): ");
        String startTimeStr = getInput("Start time (yyyy-MM-dd HH:mm:ss): ");
        String endTimeStr = getInput("End time (yyyy-MM-dd HH:mm:ss): ");
        String keyword = getInput("Message keyword: ");

        LocalDateTime startTime = null;
        if (!startTimeStr.isEmpty()) {
            Optional<LocalDateTime> parsed = parseTimestamp(startTimeStr);
            if (parsed.isEmpty()) {
                System.out.println("Invalid start time format.");
                return;
            }
            startTime = parsed.get();
        }

        LocalDateTime endTime = null;
        if (!endTimeStr.isEmpty()) {
            Optional<LocalDateTime> parsed = parseTimestamp(endTimeStr);
            if (parsed.isEmpty()) {
                System.out.println("Invalid end time format.");
                return;
            }
            endTime = parsed.get();
        }

        SearchCriteria criteria = new SearchCriteria();
        if (!level.isEmpty()) {
            criteria.setLevel(level);
        }
        criteria.setStartTime(startTime);
        criteria.setEndTime(endTime);
        if (!keyword.isEmpty()) {
            criteria.setKeyword(keyword);
        }

        if (criteria.isEmpty()) {
            System.out.println("No search criteria specified.");
            return;
        }

        LogFilter filter = new DefaultLogFilter(criteria);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String outputFileName = "combined_search_" + timestamp + ".txt";
        String fullOutputPath = OUTPUT_DIR + outputFileName;

        SearchResult result = processor.process(AppConfig.DEFAULT_LOG_PATH, filter, fullOutputPath);
        processResults(result, filter.getDescription(), "combined_search");
    }

    private void processResults(SearchResult result, String criteria, String outputPath)
          {
        System.out.println("\n=== Search Results ===");
        System.out.println("Total lines in log file: " + result.getTotalLinesProcessed());
        System.out.println("Criteria: " + criteria);
        System.out.println("Matching entries found: " + result.getMatchingCount());
        System.out.println("Search execution time: " + result.getFormattedExecutionTime());

        if (result.getMatchingCount() == 0) {
            System.out.println("No matching log entries found.");
            return;
        }

        System.out.println("Results successfully saved to: " + outputPath);
    }


    private String sanitizeFilename(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "_");
    }

    private void cleanup() {
        try {
            processor.close();
            exporter.close();
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
        scanner.close();
    }

    private Optional<LocalDateTime> parseTimestamp(String timestampStr) {
        try {
            return Optional.of(LocalDateTime.parse(timestampStr, AppConfig.DATE_TIME_FORMATTER));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}

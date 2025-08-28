package AssignmentJavaCore.service;

import AssignmentJavaCore.filters.LogFilter;
import AssignmentJavaCore.parsers.LogParser;
import AssignmentJavaCore.interfaces.LogSearchEngine;
import AssignmentJavaCore.interfaces.SearchResultExporter;
import AssignmentJavaCore.model.SearchResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class LogSearchService implements AutoCloseable{
    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LogSearchEngine searchEngine;
    private final SearchResultExporter exporter;
    private final LogParser parser;

    public LogSearchService(LogSearchEngine searchEngine, SearchResultExporter exporter, LogParser parser) {
        this.searchEngine = Objects.requireNonNull(searchEngine);
        this.exporter = Objects.requireNonNull(exporter);
        this.parser = Objects.requireNonNull(parser);
    }

    public SearchResult search(String logFilePath, List<LogFilter> filters) throws IOException {
        return searchEngine.searchLogs(logFilePath, filters);
    }

    public void exportResults(SearchResult searchResult, String outputFilePath, String searchCriteria)
            throws IOException {
        exporter.exportResults(searchResult, outputFilePath, searchCriteria);
    }

    public Optional<LocalDateTime> parseTimestamp(String timestampStr) {
        try {
            return Optional.of(LocalDateTime.parse(timestampStr, INPUT_FORMATTER));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public String getFileStats(String logFilePath) throws IOException {
        AtomicLong totalLines = new AtomicLong(0);
        AtomicLong validLines = new AtomicLong(0);

        Files.lines(Paths.get(logFilePath))
                .parallel()
                .forEach(line -> {
                    totalLines.incrementAndGet();
                    if (parser.isValidFormat(line)) {
                        validLines.incrementAndGet();
                    }
                });

        return String.format("File: %s\nTotal lines: %d\nValid log lines: %d",
                logFilePath, totalLines.get(), validLines.get());
    }

    @Override
    public void close() {
        try {
            searchEngine.close();
        } catch (Exception e) {
            // Log the exception in production
        }
        try {
            exporter.close();
        } catch (Exception e) {
            // Log the exception in production
        }
    }
}

package AssignmentJavaCore.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class LogEntry {
    private final LocalDateTime timestamp;
    private final String level;
    private final String service;
    private final String message;
    private final String originalLine;

    public LogEntry(LocalDateTime timestamp, String level, String service, String message, String originalLine) {
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
        this.level = Objects.requireNonNull(level, "Level cannot be null");
        this.service = Objects.requireNonNull(service, "Service cannot be null");
        this.message = Objects.requireNonNull(message, "Message cannot be null");
        this.originalLine = Objects.requireNonNull(originalLine, "Original line cannot be null");
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getLevel() { return level; }
    public String getService() { return service; }
    public String getMessage() { return message; }
    public String getOriginalLine() { return originalLine; }

    @Override
    public String toString() {
        return originalLine;
    }
}

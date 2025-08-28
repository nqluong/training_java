package AssignmentJavaCore.parsers;

import AssignmentJavaCore.model.LogEntry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultLogParser implements LogParser {
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\] \\[([A-Z]+)\\s*\\] \\[([^\\]]+)\\] - (.+)"
    );

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public LogEntry parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        Matcher matcher = LOG_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return null;
        }

        try {
            String timestampStr = matcher.group(1);
            String level = matcher.group(2).trim();
            String service = matcher.group(3);
            String message = matcher.group(4);

            LocalDateTime timestamp = LocalDateTime.parse(timestampStr, TIMESTAMP_FORMATTER);
            return new LogEntry(timestamp, level, service, message, line);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public boolean isValidFormat(String line) {
        return line != null && LOG_PATTERN.matcher(line).matches();
    }
}

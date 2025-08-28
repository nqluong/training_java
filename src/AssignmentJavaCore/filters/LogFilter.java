package AssignmentJavaCore.filters;

import AssignmentJavaCore.model.LogEntry;

import java.util.function.Predicate;

public interface LogFilter extends Predicate<LogEntry> {
    boolean test(LogEntry logEntry);

    String getDescription();
}

package AssignmentJavaCore.parsers;

import AssignmentJavaCore.model.LogEntry;

public interface LogParser {
    LogEntry parseLine(String line);
    boolean isValidFormat(String line);
}

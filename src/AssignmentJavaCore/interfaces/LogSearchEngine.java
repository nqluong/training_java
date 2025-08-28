package AssignmentJavaCore.interfaces;

import AssignmentJavaCore.filters.LogFilter;
import AssignmentJavaCore.model.SearchResult;

import java.io.IOException;
import java.util.List;

public interface LogSearchEngine extends AutoCloseable{
    SearchResult searchLogs(String logFilePath, List<LogFilter> filters) throws IOException;
}

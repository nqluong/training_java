package AssignmentJavaCore.exporters;

import AssignmentJavaCore.model.SearchResult;

import java.io.IOException;

public interface LogExporter extends AutoCloseable{
    void exportResults(SearchResult searchResult, String outputFilePath, String searchCriteria) throws IOException, InterruptedException;
}

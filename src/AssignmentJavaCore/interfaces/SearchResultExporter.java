package AssignmentJavaCore.interfaces;

import AssignmentJavaCore.model.SearchResult;

import java.io.IOException;

public interface SearchResultExporter extends AutoCloseable {
    void exportResults(SearchResult searchResult, String outputFilePath, String searchCriteria) throws IOException;
}

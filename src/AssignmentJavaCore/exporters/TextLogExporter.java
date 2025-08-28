package AssignmentJavaCore.exporters;

import AssignmentJavaCore.io.LogWriter;
import AssignmentJavaCore.model.SearchResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TextLogExporter implements LogExporter{
    private static final String POISON_PILL = "POISON_PILL_UNIQUE_MARKER";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void exportResults(SearchResult searchResult, String outputFilePath, String searchCriteria) throws IOException, InterruptedException {
        Path outputPath = Paths.get(Objects.requireNonNull(outputFilePath));

        BlockingQueue<String> writeQueue = new LinkedBlockingQueue<>();

        LogWriter writer = new LogWriter(writeQueue, outputFilePath, true); // append mode
        Thread writeThread = new Thread(writer);
        writeThread.start();

        // Put header
        StringBuilder header = new StringBuilder();
        header.append("=== Log Search Results ===\n");
        header.append("Search performed at: ").append(LocalDateTime.now().format(TIMESTAMP_FORMATTER)).append("\n");
        header.append("Search criteria: ").append(searchCriteria).append("\n");
        header.append("Total lines processed: ").append(searchResult.getTotalLinesProcessed()).append("\n");
        header.append("Matching results: ").append(searchResult.getMatchingCount()).append("\n");
        header.append("Search execution time: ").append(searchResult.getFormattedExecutionTime()).append("\n");
        header.append("=========================\n\n");
        writeQueue.put(header.toString());

        // Poison pill
        writeQueue.put(POISON_PILL);

        // Wait for writer to finish
        writeThread.join();
    }

    @Override
    public void close() throws Exception {

    }
}

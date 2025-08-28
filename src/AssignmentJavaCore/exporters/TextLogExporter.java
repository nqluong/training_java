package AssignmentJavaCore.exporters;

import AssignmentJavaCore.config.AppConfig;
import AssignmentJavaCore.io.LogWriter;
import AssignmentJavaCore.model.SearchResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TextLogExporter implements LogExporter{

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
        header.append("Search performed at: ").append(LocalDateTime.now().format(AppConfig.DATE_TIME_FORMATTER)).append("\n");
        header.append("Search criteria: ").append(searchCriteria).append("\n");
        header.append("Total lines processed: ").append(searchResult.getTotalLinesProcessed()).append("\n");
        header.append("Matching results: ").append(searchResult.getMatchingCount()).append("\n");
        header.append("Search execution time: ").append(searchResult.getFormattedExecutionTime()).append("\n");
        header.append("=========================\n\n");
        writeQueue.put(header.toString());

        // Poison pill
        writeQueue.put(AppConfig.POISON_PILL);

        // Wait for writer to finish
        writeThread.join();
    }

    @Override
    public void close() throws Exception {

    }
}

package AssignmentJavaCore.io;

import AssignmentJavaCore.config.AppConfig;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.BlockingQueue;

public class LogWriter implements Runnable{
    private  BlockingQueue<String> queue;
    private  String outputPath;
    private  boolean append;

    public LogWriter(BlockingQueue<String> queue, String outputPath, boolean append) {
        this.queue = queue;
        this.outputPath = outputPath;
        this.append = append;
    }

    public LogWriter(BlockingQueue<String> queue, String outputPath) {
        this(queue, outputPath, false);
    }

    @Override
    public void run() {
        StandardOpenOption[] options = append ? new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND}
                : new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath), options)) {
            while (true) {
                String line = queue.take();
                if (AppConfig.POISON_PILL.equals(line)) {
                    break;
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Writer error", e);
        }
    }
}

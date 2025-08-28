package AssignmentJavaCore.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

public class LogReader implements Runnable{
    private  Path path;
    private BlockingQueue<String> queue;
    private  String poisonPill;
    private  int consumerCount;

    public LogReader(Path path, BlockingQueue<String> queue, String poisonPill, int consumerCount) {
        this.path = path;
        this.queue = queue;
        this.poisonPill = poisonPill;
        this.consumerCount = consumerCount;
    }

    @Override
    public void run() {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                queue.put(line);
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Producer error", e);
        } finally {
            // Add poison pills for consumers
            for (int i = 0; i < consumerCount; i++) {
                try {
                    queue.put(poisonPill);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}

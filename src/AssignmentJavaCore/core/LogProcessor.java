package AssignmentJavaCore.core;

import AssignmentJavaCore.config.AppConfig;
import AssignmentJavaCore.filters.LogFilter;
import AssignmentJavaCore.io.LogReader;
import AssignmentJavaCore.model.LogEntry;
import AssignmentJavaCore.model.SearchResult;
import AssignmentJavaCore.parsers.LogParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class LogProcessor implements AutoCloseable{

    private  LogParser parser;
    private  int threadCount;
    private ExecutorService executorService;
    private AtomicLong finishedConsumers = new AtomicLong(0);
    private volatile boolean closed = false;

    public LogProcessor(LogParser parser, int threadCount) {
        this.parser = parser;
        this.threadCount = threadCount;
        this.executorService = Executors.newFixedThreadPool(threadCount + 2);
    }

    public LogProcessor(LogParser parser) {
        this(parser, Runtime.getRuntime().availableProcessors());
    }
    public SearchResult process(String logFilePath, LogFilter filter, String outputFilePath)
            throws IOException, InterruptedException, ExecutionException {

        // Check if processor is closed
        if (closed) {
            System.out.println("LogProcessor is closed");
            throw new IllegalStateException("LogProcessor has been closed");
        }

        if (executorService.isShutdown() || executorService.isTerminated()) {
            executorService = Executors.newFixedThreadPool(threadCount + 2);
        }

        synchronized (this) {
            return doProcess(logFilePath, filter, outputFilePath);
        }
    }

    public SearchResult doProcess(String logFilePath, LogFilter filter, String outputFilePath)
            throws IOException, InterruptedException, ExecutionException {

        finishedConsumers.set(0);

        long searchStartTime = System.currentTimeMillis();
        Path path = Paths.get(Objects.requireNonNull(logFilePath));

        if (!Files.exists(path)) {
            throw new IOException("Log file not found: " + logFilePath);
        }

        BlockingQueue<String> readQueue = new LinkedBlockingQueue<>(AppConfig.DEFAULT_QUEUE_SIZE);
        BlockingQueue<String> writeQueue = new LinkedBlockingQueue<>(AppConfig.DEFAULT_QUEUE_SIZE);
        AtomicLong processedLines = new AtomicLong(0);
        AtomicLong matchingCount = new AtomicLong(0);

        // Start writer thread
        Callable<Void> writerTask = createWriter(writeQueue, outputFilePath);
        Future<Void> writerFuture = executorService.submit(writerTask);
        // Producer task
        Future<?> producerFuture = executorService.submit(
                new LogReader(path, readQueue, AppConfig.POISON_PILL, threadCount)
        );

        // Consumer tasks
        List<Future<Void>> consumerFutures = IntStream.range(0, threadCount)
                .mapToObj(i -> executorService.submit(createConsumer(readQueue, filter, writeQueue, processedLines, matchingCount)))
                .toList();

        try {
            // Wait for completion
            producerFuture.get();
            for (Future<Void> future : consumerFutures) {
                future.get();
            }
            writerFuture.get();

        } catch (ExecutionException | InterruptedException e) {
            // Cancel all running tasks
            producerFuture.cancel(true);
            consumerFutures.forEach(f -> f.cancel(true));
            writerFuture.cancel(true);


            readQueue.clear();
            writeQueue.clear();

            finishedConsumers.set(0);
            throw e;
        }

        long searchEndTime = System.currentTimeMillis();
        return new SearchResult(
                searchEndTime - searchStartTime,
                processedLines.get(),
                matchingCount.get()
        );
    }

    private Callable<Void> createConsumer(
            BlockingQueue<String> readQueue,
            LogFilter filter,
            BlockingQueue<String> writeQueue,
            AtomicLong processedLines,
            AtomicLong matchingCount) {
        return () -> {
            try {
                while (true) {
                    String line = readQueue.take();
                    if (AppConfig.POISON_PILL.equals(line)) {
                        break;
                    }

                    processedLines.incrementAndGet();
                    LogEntry entry = parser.parseLine(line);
                    if (entry != null && filter.test(entry)) {
                        writeQueue.put(entry.getOriginalLine());
                        matchingCount.incrementAndGet();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Consumer error", e);
            }finally {

                long finished = finishedConsumers.incrementAndGet();
                if (finished == threadCount) {
                    try {
                        writeQueue.put(AppConfig.POISON_PILL);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            return null;
        };
    }

    private Callable<Void> createWriter(BlockingQueue<String> writeQueue, String outputPath) {
        return () -> {
            Path outPath = Paths.get(outputPath);
            Path parentDir = outPath.getParent();
            if (parentDir != null) {
                try {
                    Files.createDirectories(parentDir);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to create directories", e);
                }
            }

            try (BufferedWriter writer = Files.newBufferedWriter(outPath)) {
                while (true) {
                    String line = writeQueue.take();
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
            return null;
        };
    }

    @Override
    public void close() throws Exception {
        executorService.shutdown();
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }
}

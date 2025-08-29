package AssignmentJavaCore.core;

import AssignmentJavaCore.config.AppConfig;
import AssignmentJavaCore.exception.ConsumerException;
import AssignmentJavaCore.filters.LogFilter;
import AssignmentJavaCore.io.LogReader;
import AssignmentJavaCore.io.LogWriter;
import AssignmentJavaCore.model.LogEntry;
import AssignmentJavaCore.model.SearchResult;
import AssignmentJavaCore.parsers.LogParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class LogProcessor implements AutoCloseable {

    private LogParser parser;
    private int threadCount;
    private ExecutorService executorService;
    private AtomicLong finishedConsumers = new AtomicLong(0);
    private volatile boolean closed = false;

    /**
     * Khởi tạo LogProcessor với parser và số luồng consumer mong muốn.
     *
     * @param parser      bộ parser để phân tích cú pháp log
     * @param threadCount số lượng consumer xử lý log song song
     */
    public LogProcessor(LogParser parser, int threadCount) {
        this.parser = parser;
        this.threadCount = threadCount;
        this.executorService = Executors.newFixedThreadPool(threadCount + 2);
    }

    public LogProcessor(LogParser parser) {
        this(parser, Runtime.getRuntime().availableProcessors());
    }

    /**
     * Xử lý file log với filter và ghi kết quả ra file.
     * Phương thức này kiểm tra trạng thái processor trước khi gọi
     * và đảm bảo thread-safe thông qua synchronized.
     *
     * @param logFilePath    đường dẫn file log đầu vào
     * @param filter         điều kiện lọc log
     * @param outputFilePath đường dẫn file log kết quả
     * @return {@SearchResult} chứa thông tin thống kê sau xử lý
     * @throws IOException          nếu không tìm thấy file hoặc lỗi đọc/ghi
     * @throws InterruptedException nếu luồng bị gián đoạn
     * @throws ExecutionException   nếu có lỗi trong khi xử lý song song
     */
    public SearchResult process(String logFilePath, LogFilter filter, String outputFilePath)
            throws IOException, InterruptedException, ExecutionException {

        // Kiểm tra trạng thái của LogProcessor
        if (closed) {
            throw new IllegalStateException("LogProcessor has been closed");
        }

        if (executorService.isShutdown() || executorService.isTerminated()) {
            executorService = Executors.newFixedThreadPool(threadCount + 2);
        }

        synchronized (this) {
            return doProcess(logFilePath, filter, outputFilePath);
        }
    }

    /**
     * Thực hiện xử lý log chính (Producer-Consumers-Writer).
     *
     * @param logFilePath    đường dẫn file log
     * @param filter         điều kiện lọc log
     * @param outputFilePath file kết quả
     * @return {@SearchResult} kết quả tìm kiếm
     * @throws IOException          nếu file không tồn tại hoặc lỗi ghi
     * @throws InterruptedException nếu luồng bị gián đoạn
     * @throws ExecutionException   nếu có lỗi trong khi thực thi task
     */
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

        // Bắt đầu ghi log với thread riêng
        Callable<Void> writerTask = createWriter(writeQueue, outputFilePath);
        // Callable<Void> writerTask = new LogWriter(writeQueue, outputFilePath);
        Future<Void> writerFuture = executorService.submit(writerTask);

        // Đọc log (Producer)
        Future<?> producerFuture = executorService.submit(
                new LogReader(path, readQueue, AppConfig.POISON_PILL, threadCount)
        );

        // Xu lý log (Consumers), tạo nhiều consumer dựa trên threadCount
        List<Future<Void>> consumerFutures = IntStream.range(0, threadCount)
                .mapToObj(i -> executorService.submit(createConsumer(readQueue, filter, writeQueue, processedLines, matchingCount)))
                .toList();

        try {
            // Đợi tất cả các task hoàn thành
            producerFuture.get();
            for (Future<Void> future : consumerFutures) {
                future.get();
            }
            writerFuture.get();

        } catch (ExecutionException | InterruptedException e) {
            // Hủy các task
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
                matchingCount.get(),
                this.threadCount
        );
    }

    //Tạo một consumer xử lý log
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
                throw new ConsumerException("Consumer was interrupted", e);
            } finally {

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

    //Tạo một writer ghi kết quả ra file
    private Callable<Void> createWriter(BlockingQueue<String> writeQueue, String outputPath) {
        return new LogWriter(writeQueue, outputPath);
    }

    // Đóng LogProcessor và giải phóng tài nguyên sau 60 giây
    @Override
    public void close() throws Exception {
        executorService.shutdown();
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }
}

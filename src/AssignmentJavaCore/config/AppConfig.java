package AssignmentJavaCore.config;

import java.time.format.DateTimeFormatter;

public class AppConfig {
    private AppConfig() {};

    public static final String POISON_PILL = "POISON";
    public static final int DEFAULT_QUEUE_SIZE = 20000;

    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);

    public static final String DEFAULT_LOG_PATH = "src/AssignmentJavaCore/app.log";
    public static final String OUTPUT_DIR = "src/AssignmentJavaCore/output/";
}

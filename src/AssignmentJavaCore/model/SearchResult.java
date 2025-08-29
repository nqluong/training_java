package AssignmentJavaCore.model;


public class SearchResult {
    private long executionTime;
    private long totalLinesProcessed;
    private long matchingCount;
    private int threadCount;

    public SearchResult(long executionTime, long totalLinesProcessed, long matchingCount, int threadCount) {
        this.executionTime = executionTime;
        this.totalLinesProcessed = totalLinesProcessed;
        this.matchingCount = matchingCount;
        this.threadCount = threadCount;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public long getTotalLinesProcessed() {
        return totalLinesProcessed;
    }

    public long getMatchingCount() {
        return matchingCount;
    }

    public String getFormattedExecutionTime() {
        if (executionTime < 1000) {
            return executionTime + " ms";
        } else if (executionTime < 60000) {
            return String.format("%.2f seconds", executionTime / 1000.0);
        } else {
            long minutes = executionTime / 60000;
            long seconds = (executionTime % 60000) / 1000;
            return String.format("%d minutes %d seconds", minutes, seconds);
        }
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "executionTime=" + executionTime +
                ", totalLinesProcessed=" + totalLinesProcessed +
                ", matchingCount=" + matchingCount +
                ", threadCount=" + threadCount +
                '}';
    }
}


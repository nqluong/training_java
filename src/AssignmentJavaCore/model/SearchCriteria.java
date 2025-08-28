package AssignmentJavaCore.model;

import java.time.LocalDateTime;

public class SearchCriteria {
    private String level;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String keyword;

    public SearchCriteria() {}

    public SearchCriteria(String level, LocalDateTime startTime, LocalDateTime endTime, String keyword) {
        this.level = level;
        this.startTime = startTime;
        this.endTime = endTime;
        this.keyword = keyword;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isEmpty() {
        return level == null && startTime == null && endTime == null && keyword == null;
    }
}

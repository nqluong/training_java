package AssignmentJavaCore.filters;

import AssignmentJavaCore.model.LogEntry;
import AssignmentJavaCore.model.SearchCriteria;

public class DefaultLogFilter implements LogFilter {

    private SearchCriteria criteria;

    public DefaultLogFilter(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public boolean test(LogEntry logEntry) {
        if (criteria == null) return true;

        if (criteria.getLevel() != null && !logEntry.getLevel().equalsIgnoreCase(criteria.getLevel())) {
            return false;
        }
        if (criteria.getStartTime() != null && logEntry.getTimestamp().isBefore(criteria.getStartTime())) {
            return false;
        }
        if (criteria.getEndTime() != null && logEntry.getTimestamp().isAfter(criteria.getEndTime())) {
            return false;
        }
        if (criteria.getKeyword() != null && !logEntry.getMessage().toLowerCase().contains(criteria.getKeyword().toLowerCase())) {
            return false;
        }
        return true;
    }

    @Override
    public String getDescription() {
        if (criteria == null || criteria.isEmpty()) {
            return "No filters";
        }
        StringBuilder sb = new StringBuilder();
        if (criteria.getLevel() != null) {
            sb.append("Level: ").append(criteria.getLevel());
        }
        if (criteria.getStartTime() != null) {
            if (sb.length() > 0) sb.append("; ");
            sb.append("Start time: ").append(criteria.getStartTime());
        }
        if (criteria.getEndTime() != null) {
            if (sb.length() > 0) sb.append("; ");
            sb.append("End time: ").append(criteria.getEndTime());
        }
        if (criteria.getKeyword() != null) {
            if (sb.length() > 0) sb.append("; ");
            sb.append("Keyword: '").append(criteria.getKeyword()).append("'");
        }
        return sb.toString();
    }
}

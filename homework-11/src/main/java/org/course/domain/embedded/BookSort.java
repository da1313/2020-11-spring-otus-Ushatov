package org.course.domain.embedded;

public enum BookSort {
    NEW("time"),
    POPULAR("commentsCount"),
    BEST("avgScore");

    private final String field;

    BookSort(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

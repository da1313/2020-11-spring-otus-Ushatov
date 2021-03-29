package org.course.api.request;

import lombok.Data;

@Data
public class ScoreRequest {

    private final long bookId;

    private final int value;

}

package org.course.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
public class Card {

    @Id
    private String id;

    private User user;

    private Book book;

    private LocalDateTime start;

    private LocalDateTime end;

}

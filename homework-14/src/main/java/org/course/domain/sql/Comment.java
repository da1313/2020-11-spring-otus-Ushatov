package org.course.domain.sql;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GenericGenerator(name = "comment_id_seq", strategy = "enhanced-sequence",
            parameters = {@org.hibernate.annotations.Parameter(name = "start", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_id_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "text")
    private String text;

    @Column(name = "time")
    private LocalDateTime time;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}

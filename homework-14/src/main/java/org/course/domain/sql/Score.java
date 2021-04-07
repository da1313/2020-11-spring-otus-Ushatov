package org.course.domain.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "scores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Score {

    @Id
    @GenericGenerator(name = "score_id_seq", strategy = "enhanced-sequence",
            parameters = {@org.hibernate.annotations.Parameter(name = "start", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "score_id_seq")
    private long id;

    @Column(name = "score", nullable = false)
    private int score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

}

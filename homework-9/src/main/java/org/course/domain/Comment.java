package org.course.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "comments")
@EqualsAndHashCode(exclude = {"book", "user"})
@ToString(exclude = {"book", "user"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}

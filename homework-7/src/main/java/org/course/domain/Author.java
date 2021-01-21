package org.course.domain;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "authors")
@EqualsAndHashCode(exclude = {"books"})
@ToString(exclude = {"books"})
@Getter
@Setter
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books;
}

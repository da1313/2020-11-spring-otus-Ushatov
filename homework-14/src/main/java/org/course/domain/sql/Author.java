package org.course.domain.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "authors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    @Id
    @GenericGenerator(name = "author_id_seq", strategy = "enhanced-sequence",
            parameters = {@org.hibernate.annotations.Parameter(name = "start", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}

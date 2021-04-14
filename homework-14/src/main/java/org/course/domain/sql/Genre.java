package org.course.domain.sql;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @Id
    @GenericGenerator(name = "genre_id_seq", strategy = "enhanced-sequence",
            parameters = {@org.hibernate.annotations.Parameter(name = "start", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_id_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}

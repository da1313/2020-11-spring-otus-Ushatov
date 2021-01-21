package org.course.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
@NamedEntityGraph(name = "genre-entity-graph-with-books-author",
        attributeNodes = {@NamedAttributeNode(value = "books", subgraph = "author-subgraph")},
        subgraphs = {@NamedSubgraph(name = "author-subgraph", attributeNodes = {@NamedAttributeNode("author")})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "genres")
    private Set<Book> books = new HashSet<>();
}

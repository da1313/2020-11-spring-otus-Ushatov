package org.course.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "book_comments")
@NamedEntityGraph(name = "book-comment-entity-with-book-author",
        attributeNodes = {@NamedAttributeNode(value = "book", subgraph = "author-subgraph")},
        subgraphs = @NamedSubgraph(name = "author-subgraph", attributeNodes = @NamedAttributeNode("author")))
@Data
public class BookComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "text")
    private String text;
}

package org.course.repository;

import org.course.domain.Book;
import org.course.domain.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Book b join b.genres g where g = ?1")
    Page<Book> findAllByGenre(Genre genre, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Book b where b.title like %?1% or b.author.name like %?1%")
    Page<Book> findAllByQuery(String query, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Book b join b.genres g where g = ?1 order by (" +
                " case when (b.bookInfo.scoreOneCount + b.bookInfo.scoreTwoCount + b.bookInfo.scoreThreeCount + b.bookInfo.scoreFourCount + b.bookInfo.scoreFiveCount) = 0" +
                    " then 0" +
                " else (" +
                    " cast((b.bookInfo.scoreOneCount + b.bookInfo.scoreTwoCount * 2 + b.bookInfo.scoreThreeCount * 3 + b.bookInfo.scoreFourCount * 4 + b.bookInfo.scoreFiveCount * 5) as double)" +
                    " /" +
                    " (b.bookInfo.scoreOneCount + b.bookInfo.scoreTwoCount + b.bookInfo.scoreThreeCount + b.bookInfo.scoreFourCount + b.bookInfo.scoreFiveCount)" +
                " ) end" +
            ") desc")
    Page<Book> findAllByGenreSortedByAvgScore(Genre genre, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Book b order by (" +
            " case when (b.bookInfo.scoreOneCount + b.bookInfo.scoreTwoCount + b.bookInfo.scoreThreeCount + b.bookInfo.scoreFourCount + b.bookInfo.scoreFiveCount) = 0" +
            " then 0" +
            " else (" +
            " cast((b.bookInfo.scoreOneCount + b.bookInfo.scoreTwoCount * 2 + b.bookInfo.scoreThreeCount * 3 + b.bookInfo.scoreFourCount * 4 + b.bookInfo.scoreFiveCount * 5) as double)" +
            " /" +
            " (b.bookInfo.scoreOneCount + b.bookInfo.scoreTwoCount + b.bookInfo.scoreThreeCount + b.bookInfo.scoreFourCount + b.bookInfo.scoreFiveCount)" +
            " ) end" +
            ") desc")
    Page<Book> findAllSortedByAvgScore(Pageable pageable);

    @EntityGraph(attributePaths = {"author", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Book b where b.id = ?1")
    Optional<Book> findByIdEager(long id);

    @EntityGraph(attributePaths = {"author", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Book b")
    Page<Book> findAllEager(Pageable pageable);

}

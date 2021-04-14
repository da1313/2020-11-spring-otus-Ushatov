package org.course.steps.writer;

import lombok.RequiredArgsConstructor;
import org.course.domain.sql.Book;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class CustomBookReader extends AbstractPagingItemReader<Book> {

    private final EntityManager entityManager;

    @Override
    protected void doOpen() throws Exception {
        super.doOpen();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
    }

    @Override
    protected void doReadPage() {

        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        }
        else {
            results.clear();
        }

        List<Book> bookList = entityManager.createQuery("select b from Book b join fetch b.author order by b.id", Book.class)
                .setMaxResults(getPageSize())
                .setFirstResult(getPage() * getPageSize()).getResultList();

        List<Book> bookListWithFetchedGenres = entityManager
                .createQuery("select distinct b from Book b left join fetch b.genres where b in :bookList order by b.id", Book.class)
                .setParameter("bookList", bookList)
                .getResultList();

        results.addAll(bookListWithFetchedGenres);
    }

    @Override
    protected void doJumpToPage(int itemIndex) {

    }

    @Override
    protected void doClose() throws Exception {
        super.doClose();
    }
}

package org.course.repository.projections;

import org.course.domain.Book;

public interface BookStatistics {

    Book getBook();

    long getCommentCount();

    long getOne();

    long getTwo();

    long getThree();

    long getFour();

    long getFive();

}

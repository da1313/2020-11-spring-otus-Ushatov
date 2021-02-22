package org.course.service.interfaces;

import org.springframework.transaction.annotation.Transactional;

public interface CommentHandleService {

    void createComment(long id, String text);

}

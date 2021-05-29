package org.course.repository.custom;

import lombok.RequiredArgsConstructor;
import org.course.model.Comment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final MongoOperations mongoOperations;

    @Override
    public void updateUserComments(String id, String firstName, String lastName, String avatarUrl){
        mongoOperations.updateMulti(Query.query(Criteria.where("user.id").is(id)), new Update().set("user.firstName", firstName)
                .set("user.LastName", lastName).set("user.avatarUrl", avatarUrl), Comment.class);
    }

}

package org.course.repository;

import org.course.model.Post;
import org.course.model.User;

import java.util.List;

public interface DataBean {

    List<User> getUsers();

    List<Post> getPosts();

}

package org.course.repository;

import org.course.model.Post;
import org.course.model.User;

import java.util.ArrayList;
import java.util.List;

public class DataBeanImpl implements DataBean{

    private final List<User> userList = new ArrayList<>();
    private final List<Post> postList = new ArrayList<>();

    @Override
    public List<User> getUsers() {
        return userList;
    }

    @Override
    public List<Post> getPosts() {
        return postList;
    }
}

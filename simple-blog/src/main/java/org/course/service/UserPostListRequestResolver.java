package org.course.service;

import org.course.api.request.RequestParams;
import org.course.api.response.PostByUserListResponse;
import org.course.model.UserPostStatus;

public interface UserPostListRequestResolver {

    PostByUserListResponse getUserPosts(String userId, RequestParams params);

    UserPostStatus getType();

}

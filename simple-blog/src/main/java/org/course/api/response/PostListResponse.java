package org.course.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course.model.Post;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponse {

    private List<Post> postList;

    private int totalPages;

}

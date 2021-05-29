package org.course.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostByUserListResponse {

    private List<PostByUser> postByUserList;

    private int page;

    private long totalElements;

}

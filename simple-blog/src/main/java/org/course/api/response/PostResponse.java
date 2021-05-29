package org.course.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course.model.Post;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Post post;

    private Boolean isVote;

    private Boolean voteValue;

}

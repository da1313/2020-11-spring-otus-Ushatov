package org.course.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostStatistics {

    private int likeCount;

    private int dislikeCount;

    private int commentCount;

    private int viewCount;

    public void addLike(){
        likeCount++;
    }

    public void removeLike(){
        likeCount--;
    }

    public void addDislike(){
        dislikeCount++;
    }

    public void removeDislike(){
        dislikeCount--;
    }

    public void addComment(){
        commentCount++;
    }

    public void addView(){
        viewCount++;
    }

}

package org.course.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCardDataListResponse {

    private List<PostCartData> postCartDataList;

    private int totalPages;

}

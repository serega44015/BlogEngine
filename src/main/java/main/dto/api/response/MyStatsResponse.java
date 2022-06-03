package main.dto.api.response;

import lombok.Data;

@Data
public class MyStatsResponse {

    private Integer postsCount;
    private Integer likesCount;
    private Integer dislikesCount;
    private Integer viewsCount;
    private Long firstPublication;

}

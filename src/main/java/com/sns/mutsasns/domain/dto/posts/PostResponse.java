package com.sns.mutsasns.domain.dto.posts;


import com.sns.mutsasns.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String body;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //entity -> dto
    public PostResponse(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.userName = post.getUser().getUserName();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

    }
}

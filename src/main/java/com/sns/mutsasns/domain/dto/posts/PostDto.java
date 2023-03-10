package com.sns.mutsasns.domain.dto.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostDto {
    private String message;
    private Long postId;
    private String title;
    private String body;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostResponse toResponse(){
        return PostResponse.builder()
                .id(this.postId)
                .title(this.title)
                .body(this.body)
                .userName(this.userName)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}

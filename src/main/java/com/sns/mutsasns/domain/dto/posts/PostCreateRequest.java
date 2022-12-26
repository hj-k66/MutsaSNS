package com.sns.mutsasns.domain.dto.posts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostCreateRequest {
    private String title;
    private String body;
}

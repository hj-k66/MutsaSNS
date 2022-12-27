package com.sns.mutsasns.domain.dto.posts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostWriteResponse {
    private String message;
    private Long postId;
}

package com.sns.mutsasns.domain.dto.posts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostWriteRequest {
    private String title;
    private String body;
}

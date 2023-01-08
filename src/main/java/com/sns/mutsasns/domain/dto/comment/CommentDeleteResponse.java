package com.sns.mutsasns.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CommentDeleteResponse {
    private String message;
    private Long commentId;
}

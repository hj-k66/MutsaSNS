package com.sns.mutsasns.domain.dto.comment;

import com.sns.mutsasns.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String comment;
    private String userName;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    //entity -> dto
    public CommentResponse(Comment comment){
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.userName = comment.getUser().getUserName();
        this.postId = comment.getPost().getId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}

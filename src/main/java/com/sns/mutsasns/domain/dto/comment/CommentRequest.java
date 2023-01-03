package com.sns.mutsasns.domain.dto.comment;

import com.sns.mutsasns.domain.entity.Comment;
import com.sns.mutsasns.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentRequest {
    private String comment;

    // dto -> entity
    public Comment toEntity(Post post){
        System.out.println(comment);
        return Comment.builder()
                .comment(this.comment)
                .user(post.getUser())
                .post(post)
                .build();
    }
}

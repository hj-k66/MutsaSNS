package com.sns.mutsasns.domain.dto.comment;

import com.sns.mutsasns.domain.entity.Comment;
import com.sns.mutsasns.domain.entity.Post;
import com.sns.mutsasns.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class CommentRequest {
    private String comment;

    // dto -> entity
    public Comment toEntity(Post post, User user){
        System.out.println(comment);
        return Comment.builder()
                .comment(this.comment)
                .user(user)
                .post(post)
                .build();
    }
}

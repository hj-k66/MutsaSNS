package com.sns.mutsasns.fixture;

import com.sns.mutsasns.domain.entity.Comment;

public class CommentFixture {
    public static Comment get(String commentContent) {
        return Comment.builder()
                .id(1L)
                .comment(commentContent)
                .user(UserFixture.get(TestInfoFixture.get().getUserName(), TestInfoFixture.get().getPassword()))
                .post(PostFixture.get(TestInfoFixture.get().getUserName(), TestInfoFixture.get().getPassword()))
                .build();
    }
}

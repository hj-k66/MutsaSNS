package com.sns.mutsasns.fixture;

import com.sns.mutsasns.domain.entity.Post;

public class PostFixture {
    public static Post get(String userName, String password) {
        Post post = Post.builder()
                .id(1L)
                .user(UserFixture.get(userName, password))
                .title("title")
                .body("body")
                .build();
        return post;
    }


}

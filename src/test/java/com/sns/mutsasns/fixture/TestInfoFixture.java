package com.sns.mutsasns.fixture;

import lombok.Getter;
import lombok.Setter;

public class TestInfoFixture {
    public static TestInfo get() {
        TestInfo info = new TestInfo();
        info.setPostId(1L);
        info.setUserId(1L);
        info.setUserName("user");
        info.setPassword("password");
        info.setTitle("title");
        info.setBody("body");
        info.setCommentId(1L);
        return info;
    }

    @Getter
    @Setter
    public static class TestInfo {
        private Long postId;
        private Long userId;
        private String userName;
        private String password;
        private String title;
        private String body;
        private Long commentId;
    }

}


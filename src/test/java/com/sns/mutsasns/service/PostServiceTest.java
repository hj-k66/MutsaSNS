package com.sns.mutsasns.service;

import com.sns.mutsasns.domain.dto.posts.PostWriteRequest;
import com.sns.mutsasns.domain.dto.posts.PostDto;
import com.sns.mutsasns.domain.entity.Post;
import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.SNSException;
import com.sns.mutsasns.fixture.PostFixture;
import com.sns.mutsasns.fixture.TestInfoFixture;
import com.sns.mutsasns.fixture.UserFixture;
import com.sns.mutsasns.respository.PostRepository;
import com.sns.mutsasns.respository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostServiceTest {

    PostService postService;

    PostRepository postRepository = mock(PostRepository.class);
    UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository);
    }

    @Test
    @DisplayName("수정 실패 - 포스트 존재 x")
    void modify_failed_post_not_found() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());

        PostWriteRequest postWriteRequest = new PostWriteRequest(fixture.getTitle(), fixture.getBody());
        SNSException exception = Assertions.assertThrows(SNSException.class, () -> postService.modify(fixture.getPostId(), postWriteRequest, fixture.getUserName()));

        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }
    @Test
    @DisplayName("수정 실패 - 유저 존재 x")
    void modify_failed_user_not_found() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(userRepository.findByUserName(fixture.getUserName()))
                .thenReturn(Optional.empty());
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(Post.class)));

        PostWriteRequest postWriteRequest = new PostWriteRequest(fixture.getTitle(), fixture.getBody());
        SNSException exception = Assertions.assertThrows(SNSException.class, () -> postService.modify(fixture.getPostId(), postWriteRequest, fixture.getUserName()));

        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("수정 실패 - 작성자 != 유저")
    void modify_failed_not_equal_users() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user1 = UserFixture.get("user1", "asdf");
        User user2 = UserFixture.get("user2", "alksdjf");

        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(Post.builder()
                .title(fixture.getTitle())
                .body(fixture.getBody())
                .user(user1)
                .build()));
        when(userRepository.findByUserName(user2.getUserName()))
                .thenReturn(Optional.of(User.builder()
                        .userName(user2.getUserName())
                        .password(user2.getPassword())
                        .build()));

        PostWriteRequest postWriteRequest = new PostWriteRequest(fixture.getTitle(), fixture.getBody());
        SNSException exception = Assertions.assertThrows(SNSException.class, () -> postService.modify(fixture.getPostId(), postWriteRequest, user2.getUserName()));

        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @DisplayName("등록 성공")
    void post_success() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        Post mockPostEntity = mock(Post.class);
        User mockUserEntity = mock(User.class);

        when(userRepository.findByUserName(fixture.getUserName()))
                .thenReturn(Optional.of(mockUserEntity));
        when(postRepository.save(any()))
                .thenReturn(mockPostEntity);
        PostWriteRequest postWriteRequest = new PostWriteRequest(fixture.getTitle(), fixture.getBody());
        Assertions.assertDoesNotThrow(() -> postService.create(postWriteRequest, fixture.getUserName()));
    }

    @Test
    @DisplayName("등록 실패 - 유저가 존재하지 않음")
    void post_failed_NoUser() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(userRepository.findByUserName(fixture.getUserName()))
                .thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        PostWriteRequest postWriteRequest = new PostWriteRequest(fixture.getTitle(), fixture.getBody());
        SNSException exception = Assertions.assertThrows(SNSException.class, () -> postService.create(postWriteRequest, fixture.getUserName()));

        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());

    }

    @Test
    @DisplayName("포스트 상세 조회 성공")
    void get_one_post_success() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = User.builder()
                .userName(fixture.getUserName())
                .build();
        Post post = PostFixture.get(fixture.getUserName(), "asdf");

        when(postRepository.findById(fixture.getPostId()))
                .thenReturn(Optional.of(post));
        PostDto postDto = postService.getOnePost(fixture.getPostId());

        Assertions.assertEquals(postDto.getUserName(), fixture.getUserName());
    }

}
package com.sns.mutsasns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.mutsasns.configuration.EncrypterConfig;
import com.sns.mutsasns.domain.dto.posts.PostWriteRequest;
import com.sns.mutsasns.domain.dto.posts.PostDto;

import com.sns.mutsasns.domain.entity.Post;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.SNSException;
import com.sns.mutsasns.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EncrypterConfig EncrypterConfig;

    @Test
    @DisplayName("포스트 수정 성공")
    @WithMockUser
    void modifyPost_success() throws Exception {
        PostWriteRequest postWriteRequest = new PostWriteRequest("수정제목", "수정내용");

        Post post = Post.builder()
                .id(1L)
                .build();

        when(postService.modify(any(), any(), any()))
                .thenReturn(PostDto.builder()
                        .message("포스트 수정 완료")
                        .postId(post.getId())
                        .build());

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postWriteRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포스트 수정 실패 - 인증 실패")
    @WithAnonymousUser
    void modifyPost_failed() throws Exception {
        PostWriteRequest postWriteRequest = new PostWriteRequest("제목제목수정", "내용내용수정");

        //Controller에서는 JWT Filter Exception을 검증x
//        when(postService.modify(any(),any(),any())).thenThrow(new SNSException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postWriteRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 수정 실패 - 작성자 불일치")
    @WithMockUser
    void modifyPost_failed_user() throws Exception{
        PostWriteRequest postWriteRequest = new PostWriteRequest("제목제목수정", "내용내용수정");

        when(postService.modify(any(),any(),any())).thenThrow(new SNSException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postWriteRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @DisplayName("포스트 수정 실패 - 데이터베이스 에러")
    @WithMockUser
    void modifyPost_failed_db() throws Exception{
        PostWriteRequest postWriteRequest = new PostWriteRequest("제목제목수정", "내용내용수정");

        when(postService.modify(any(),any(),any())).thenThrow(new SNSException(ErrorCode.DATABASE_ERROR));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postWriteRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
    }


    @Test
    @DisplayName("포스트 작성 성공")
    @WithMockUser
    void createPost_success() throws Exception {
        PostWriteRequest postWriteRequest = new PostWriteRequest("제목제목", "내용내용");

        when(postService.create(any(), any())).thenReturn(mock(PostDto.class));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postWriteRequest)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("포스트 작성 실패 - 로그인 하지 않은 경우 ")
    @WithAnonymousUser
    void createPost_failed() throws Exception {
        PostWriteRequest postWriteRequest = new PostWriteRequest("제목제목", "내용내용");

        //Controller에서는 JWT Filter Exception을 검증x
//        when(postService.create(any(),any())).thenThrow(new SNSException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postWriteRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 상세 조회 성공")
    @WithMockUser
    void getOnePost_success() throws Exception {
        PostDto postDto = PostDto.builder()
                .postId(1L)
                .title("제목목")
                .body("내요용")
                .userName("heejung")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(postService.getOnePost(any())).thenReturn(postDto);

        mockMvc.perform(get("/api/v1/posts/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.title").exists())
                .andExpect(jsonPath("$.result.body").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.updatedAt").exists());

    }

    @Test
    @DisplayName("포스트 목록 조회 성공")
    @WithMockUser
    void getPostList_success() throws Exception {
        when(postService.getAllPosts(any())).thenReturn(mock(Page.class));

        mockMvc.perform(get("/api/v1/posts")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "createdAt,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(postService).getAllPosts(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        Assertions.assertEquals(0, pageable.getPageNumber());
        Assertions.assertEquals(3, pageable.getPageSize());
        Assertions.assertEquals(Sort.by("createdAt", "desc"), pageable.withSort(Sort.by("createdAt", "desc")).getSort());

    }
}
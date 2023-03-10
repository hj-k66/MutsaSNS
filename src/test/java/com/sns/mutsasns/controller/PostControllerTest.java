package com.sns.mutsasns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.mutsasns.domain.dto.comment.CommentDeleteResponse;
import com.sns.mutsasns.domain.dto.comment.CommentRequest;
import com.sns.mutsasns.domain.dto.comment.CommentResponse;
import com.sns.mutsasns.domain.dto.like.LikeCountResponse;
import com.sns.mutsasns.domain.dto.like.LikeResponse;
import com.sns.mutsasns.domain.dto.posts.PostWriteRequest;
import com.sns.mutsasns.domain.dto.posts.PostDto;

import com.sns.mutsasns.domain.entity.Post;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.SNSException;
import com.sns.mutsasns.fixture.CommentFixture;
import com.sns.mutsasns.fixture.TestInfoFixture;
import com.sns.mutsasns.service.CommentService;
import com.sns.mutsasns.service.LikeService;
import com.sns.mutsasns.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import static org.mockito.BDDMockito.given;
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
    @MockBean
    CommentService commentService;
    @MockBean
    LikeService likeService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class LikeTest{
        @Nested
        class CreateTest{
            @Test
            @DisplayName("????????? ????????? ??????")
            @WithMockUser
            void create_like_success() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                Long postId = fixture.getPostId();
                String message = fixture.getUserName() + "?????? ???????????? ???????????????.";
                LikeResponse likeResponse = new LikeResponse(message);

                given(likeService.createLike(postId,fixture.getUserName())).willReturn(likeResponse);
                //when-then
                mockMvc.perform(post("/api/v1/posts/" + postId +"/likes")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result").value(likeResponse.getMessage()));
            }

            @Test
            @DisplayName("????????? ????????? ?????? - ????????? ?????? ?????? ??????")
            @WithAnonymousUser
            void create_like_fail_no_login()throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                Long postId = fixture.getPostId();

                //when-then
                mockMvc.perform(post("/api/v1/posts/" + postId +"/likes")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("????????? ????????? ?????? - ?????? ???????????? ?????? ??????")
            @WithMockUser
            void create_like_fail_no_post() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                Long postId = fixture.getPostId();

                given(likeService.createLike(postId,fixture.getUserName())).willThrow(new SNSException(ErrorCode.POST_NOT_FOUND));
                //when-then
                mockMvc.perform(post("/api/v1/posts/" + postId +"/likes")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()))
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
                        .andExpect(jsonPath("$.result.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));
            }
        }
        @Nested
        class GetTest{
            @Test
            @DisplayName("????????? ?????? ?????? ??????")
            @WithMockUser
            void get_like_count_success() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                Long postId = fixture.getPostId();
                LikeCountResponse likeCountResponse = new LikeCountResponse(1);

                given(likeService.getLikeCount(postId)).willReturn(likeCountResponse);

                //when-then
                mockMvc.perform(get("/api/v1/posts/" + postId +"/likes")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result").value(likeCountResponse.getLikeCount()));

            }

            @Test
            @DisplayName("????????? ?????? ?????? ?????? - ?????? ???????????? ?????? ??????")
            @WithMockUser
            void get_like_count_fail_no_post() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                Long postId = fixture.getPostId();

                given(likeService.getLikeCount(postId)).willThrow(new SNSException(ErrorCode.POST_NOT_FOUND));
                //when-then
                mockMvc.perform(get("/api/v1/posts/" + postId +"/likes")
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()))
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
                        .andExpect(jsonPath("$.result.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));
            }
        }



    }
    @Nested
    class MyFeedTest{
        @Test
        @DisplayName("???????????? ?????? ??????")
        @WithMockUser
        void getMyFeed_success() throws Exception {
            when(postService.getMyFeed(any(), any())).thenReturn(Page.empty());

            mockMvc.perform(get("/api/v1/posts/my")
                            .param("page", "0")
                            .param("size", "20")
                            .param("sort", "createdAt,desc"))
                    .andExpect(status().isOk())
                    .andDo(print());

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

            verify(postService).getMyFeed(any(),pageableCaptor.capture());
            PageRequest pageable = (PageRequest) pageableCaptor.getValue();
            System.out.println(pageable.toString());


            Assertions.assertEquals(0, pageable.getPageNumber());
            Assertions.assertEquals(20, pageable.getPageSize());
            Assertions.assertEquals(Sort.by("createdAt", "desc"), pageable.withSort(Sort.by("createdAt", "desc")).getSort());

        }

        @Test
        @DisplayName("???????????? ?????? ?????? - ????????? ?????? ?????? ??????")
        @WithAnonymousUser
        void getMyFeed_fail_no_login() throws Exception {
            when(postService.getMyFeed(any(), any())).thenReturn(Page.empty());

            mockMvc.perform(get("/api/v1/posts/my"))
                    .andExpect(status().isUnauthorized())
                    .andDo(print());

        }

    }

    @Nested
    class CommentTest{
        @Nested
        class CreateTest{
            @Test
            @DisplayName("?????? ?????? ??????")
            @WithMockUser(username = "user")
            void createComment_success() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                String comment = "????????????";
                Long postId = fixture.getPostId();
                CommentRequest commentRequest = new CommentRequest(comment);
                CommentResponse commentResponse = new CommentResponse(CommentFixture.get(comment));

                given(commentService.create(postId,commentRequest, fixture.getUserName())).willReturn(commentResponse);

                //when - then
                mockMvc.perform(post("/api/v1/posts/" + postId +"/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result.id").value(commentResponse.getId()))
                        .andExpect(jsonPath("$.result.comment").value(commentResponse.getComment()))
                        .andExpect(jsonPath("$.result.userName").value(commentResponse.getUserName()))
                        .andExpect(jsonPath("$.result.postId").value(commentResponse.getPostId()));

            }
            @Test
            @DisplayName("?????? ?????? ?????? - ????????? ?????? ?????? ??????")
            @WithAnonymousUser
            void createComment_failed_not_login() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                String comment = "????????????";
                Long postId = fixture.getPostId();
                CommentRequest commentRequest = new CommentRequest(comment);


                //when - then
                mockMvc.perform(post("/api/v1/posts/" + postId +"/comments")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(commentRequest)))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("?????? ?????? ?????? - ????????? ???????????? ?????? ??????")
            @WithMockUser
            void createComment_failed_no_post() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                String comment = "????????????";
                Long postId = 100L;
                CommentRequest commentRequest = new CommentRequest(comment);

                given(commentService.create(postId,commentRequest, fixture.getUserName())).willThrow(new SNSException(ErrorCode.POST_NOT_FOUND));

                //when - then
                mockMvc.perform(post("/api/v1/posts/" + postId +"/comments")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(commentRequest)))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.resultCode").value("ERROR"))
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
                        .andExpect(jsonPath("$.result.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));

            }
        }

        @Nested
        class modifyTest{
            @Test
            @DisplayName("?????? ?????? ??????")
            @WithMockUser(username = "user")
            void modifyComment_success() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                String comment = "????????????";
                Long postId = fixture.getPostId();
                Long commentId = fixture.getCommentId();
                CommentRequest commentRequest = new CommentRequest(comment);
                CommentResponse commentResponse = new CommentResponse(CommentFixture.get(comment));

                given(commentService.modify(postId,commentId,commentRequest, fixture.getUserName())).willReturn(commentResponse);

                //when - then
                mockMvc.perform(put("/api/v1/posts/" + postId +"/comments/" + commentId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(commentRequest)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result.id").value(commentResponse.getId()))
                        .andExpect(jsonPath("$.result.comment").value(commentResponse.getComment()))
                        .andExpect(jsonPath("$.result.userName").value(commentResponse.getUserName()))
                        .andExpect(jsonPath("$.result.postId").value(commentResponse.getPostId()))
                        .andExpect(jsonPath("$.result.createdAt").value(commentResponse.getCreatedAt()))
                        .andExpect(jsonPath("$.result.updatedAt").value(commentResponse.getUpdatedAt()));
            }

            @Test
            @DisplayName("?????? ?????? ?????? - ?????? ??????")
            @WithAnonymousUser
            void modifyComment_fail_No_login() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                String comment = "????????????";
                Long postId = fixture.getPostId();
                Long commentId = fixture.getCommentId();
                CommentRequest commentRequest = new CommentRequest(comment);


                //when - then
                mockMvc.perform(put("/api/v1/posts/" + postId +"/comments/" + commentId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(commentRequest)))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());

            }

            @Test
            @DisplayName("?????? ?????? ?????? - ????????? ???????????? ?????? ??????")
            @WithMockUser
            void modifyComment_failed_no_post() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                String comment = "????????????";
                Long postId = 100L;
                Long commentId = fixture.getCommentId();
                CommentRequest commentRequest = new CommentRequest(comment);

                given(commentService.modify(postId,commentId, commentRequest, fixture.getUserName())).willThrow(new SNSException(ErrorCode.POST_NOT_FOUND));

                //when - then
                mockMvc.perform(put("/api/v1/posts/" + postId +"/comments/" + commentId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(commentRequest)))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.resultCode").value("ERROR"))
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
                        .andExpect(jsonPath("$.result.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));

            }

            @Test
            @DisplayName("?????? ?????? ?????? - ????????? ?????????")
            @WithMockUser(username = "user")
            void modifyComment_failed_not_equal_user() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                String comment = "????????????";
                Long postId = fixture.getPostId();
                Long commentId = fixture.getCommentId();
                CommentRequest commentRequest = new CommentRequest(comment);

                given(commentService.modify(postId,commentId, commentRequest, fixture.getUserName())).willThrow(new SNSException(ErrorCode.INVALID_PERMISSION));

                //when - then
                mockMvc.perform(put("/api/v1/posts/" + postId +"/comments/" + commentId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(commentRequest)))
                        .andDo(print())
                        .andExpect(status().isUnauthorized())
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.resultCode").value("ERROR"))
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.INVALID_PERMISSION.name()))
                        .andExpect(jsonPath("$.result.message").value(ErrorCode.INVALID_PERMISSION.getMessage()));

            }

            @Test
            @DisplayName("?????? ?????? ?????? - ?????????????????? ??????")
            @WithMockUser(username = "user")
            void modifyComment_failed_db() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                String comment = "????????????";
                Long postId = fixture.getPostId();
                Long commentId = fixture.getCommentId();
                CommentRequest commentRequest = new CommentRequest(comment);

                given(commentService.modify(postId,commentId, commentRequest, fixture.getUserName())).willThrow(new SNSException(ErrorCode.DATABASE_ERROR));

                //when - then
                mockMvc.perform(put("/api/v1/posts/" + postId +"/comments/" + commentId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(commentRequest)))
                        .andDo(print())
                        .andExpect(status().isInternalServerError())
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.resultCode").value("ERROR"))
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.DATABASE_ERROR.name()))
                        .andExpect(jsonPath("$.result.message").value(ErrorCode.DATABASE_ERROR.getMessage()));

            }
        }

        @Nested
        class deleteTest{
            @Test
            @DisplayName("?????? ?????? ??????")
            @WithMockUser(username = "user")
            void deleteComment_success() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                Long postId = fixture.getPostId();
                Long commentId = fixture.getCommentId();
                CommentDeleteResponse commentDeleteResponse = new CommentDeleteResponse("?????? ?????? ??????", commentId);

                given(commentService.delete(postId,commentId,fixture.getUserName())).willReturn(commentDeleteResponse);

                //when - then
                mockMvc.perform(delete("/api/v1/posts/" + postId +"/comments/" + commentId)
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result.message").value("?????? ?????? ??????"))
                        .andExpect(jsonPath("$.result.commentId").value(commentId));
            }

            @Test
            @DisplayName("?????? ?????? ?????? - ?????? ??????")
            @WithAnonymousUser
            void deleteComment_fail_No_login() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                Long postId = fixture.getPostId();
                Long commentId = fixture.getCommentId();


                //when - then
                mockMvc.perform(put("/api/v1/posts/" + postId +"/comments/" + commentId)
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());

            }

            @Test
            @DisplayName("?????? ?????? ?????? - ????????? ???????????? ?????? ??????")
            @WithMockUser
            void deleteComment_failed_no_post() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                Long postId = 100L;
                Long commentId = fixture.getCommentId();

                given(commentService.delete(postId,commentId, fixture.getUserName())).willThrow(new SNSException(ErrorCode.POST_NOT_FOUND));

                //when - then
                mockMvc.perform(delete("/api/v1/posts/" + postId +"/comments/" + commentId)
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()))
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.resultCode").value("ERROR"))
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
                        .andExpect(jsonPath("$.result.message").value(ErrorCode.POST_NOT_FOUND.getMessage()));

            }

            @Test
            @DisplayName("?????? ?????? ?????? - ????????? ?????????")
            @WithMockUser(username = "user")
            void deleteComment_failed_not_equal_user() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                Long postId = fixture.getPostId();
                Long commentId = fixture.getCommentId();

                given(commentService.delete(postId,commentId, fixture.getUserName())).willThrow(new SNSException(ErrorCode.INVALID_PERMISSION));

                //when - then
                mockMvc.perform(delete("/api/v1/posts/" + postId +"/comments/" + commentId)
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()))
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.resultCode").value("ERROR"))
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.INVALID_PERMISSION.name()))
                        .andExpect(jsonPath("$.result.message").value(ErrorCode.INVALID_PERMISSION.getMessage()));

            }

            @Test
            @DisplayName("?????? ?????? ?????? - ?????????????????? ??????")
            @WithMockUser(username = "user")
            void deleteComment_failed_db() throws Exception {
                //given
                TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
                Long postId = fixture.getPostId();
                Long commentId = fixture.getCommentId();

                given(commentService.delete(postId,commentId, fixture.getUserName())).willThrow(new SNSException(ErrorCode.DATABASE_ERROR));

                //when - then
                mockMvc.perform(delete("/api/v1/posts/" + postId +"/comments/" + commentId)
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()))
                        .andExpect(jsonPath("$.resultCode").exists())
                        .andExpect(jsonPath("$.resultCode").value("ERROR"))
                        .andExpect(jsonPath("$.result").exists())
                        .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.DATABASE_ERROR.name()))
                        .andExpect(jsonPath("$.result.message").value(ErrorCode.DATABASE_ERROR.getMessage()));

            }
        }

        @Nested
        class GetTest{
            @Test
            @DisplayName("?????? ?????? ?????? ??????")
            @WithMockUser
            void getCommentList_success() throws Exception {
                when(commentService.getAllComments(any(), any())).thenReturn(Page.empty());

                mockMvc.perform(get("/api/v1/posts/1/comments")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "createdAt,desc"))
                        .andExpect(status().isOk())
                        .andDo(print());

                ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

                verify(commentService).getAllComments(any(),pageableCaptor.capture());
                PageRequest pageable = (PageRequest) pageableCaptor.getValue();
                System.out.println(pageable.toString());


                Assertions.assertEquals(0, pageable.getPageNumber());
                Assertions.assertEquals(10, pageable.getPageSize());
                Assertions.assertEquals(Sort.by("createdAt", "desc"), pageable.withSort(Sort.by("createdAt", "desc")).getSort());

            }
        }



    }

    @Nested
    class PostTest{
        @Nested
        class DeleteTest{
            @Test
            @DisplayName("????????? ?????? ??????")
            @WithMockUser
            void deletePost_success() throws Exception{
                Long postId = 1L;

                when(postService.delete(any(), any())).thenReturn(PostDto.builder()
                        .message("????????? ?????? ??????")
                        .postId(postId)
                        .build());

                mockMvc.perform(delete("/api/v1/posts/" + postId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk());
            }

            @Test
            @DisplayName("????????? ?????? ?????? - ?????? ??????")
            @WithAnonymousUser
            void deletePost_failed() throws Exception {
                long postId = 1L;

                //Controller????????? JWT Filter Exception??? ??????x
//        when(postService.delete(any(),any(),any())).thenThrow(new SNSException(ErrorCode.INVALID_PERMISSION));

                mockMvc.perform(put("/api/v1/posts/" + postId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("????????? ?????? ?????? - ????????? ?????????")
            @WithMockUser
            void deletePost_failed_user() throws Exception{

                when(postService.delete(any(),any())).thenThrow(new SNSException(ErrorCode.INVALID_PERMISSION));

                mockMvc.perform(delete("/api/v1/posts/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
            }

            @Test
            @DisplayName("????????? ?????? ?????? - ?????????????????? ??????")
            @WithMockUser
            void deletePost_failed_db() throws Exception{

                when(postService.delete(any(),any())).thenThrow(new SNSException(ErrorCode.DATABASE_ERROR));

                mockMvc.perform(delete("/api/v1/posts/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
            }

        }


        @Nested
        class ModifyTest{
            @Test
            @DisplayName("????????? ?????? ??????")
            @WithMockUser
            void modifyPost_success() throws Exception {
                PostWriteRequest postWriteRequest = new PostWriteRequest("????????????", "????????????");

                Post post = Post.builder()
                        .id(1L)
                        .build();

                when(postService.modify(any(), any(), any()))
                        .thenReturn(PostDto.builder()
                                .message("????????? ?????? ??????")
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
            @DisplayName("????????? ?????? ?????? - ?????? ??????")
            @WithAnonymousUser
            void modifyPost_failed() throws Exception {
                PostWriteRequest postWriteRequest = new PostWriteRequest("??????????????????", "??????????????????");

                //Controller????????? JWT Filter Exception??? ??????x
//        when(postService.modify(any(),any(),any())).thenThrow(new SNSException(ErrorCode.INVALID_PERMISSION));

                mockMvc.perform(put("/api/v1/posts/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(postWriteRequest)))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("????????? ?????? ?????? - ????????? ?????????")
            @WithMockUser
            void modifyPost_failed_user() throws Exception{
                PostWriteRequest postWriteRequest = new PostWriteRequest("??????????????????", "??????????????????");

                when(postService.modify(any(),any(),any())).thenThrow(new SNSException(ErrorCode.INVALID_PERMISSION));

                mockMvc.perform(put("/api/v1/posts/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(postWriteRequest)))
                        .andDo(print())
                        .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
            }

            @Test
            @DisplayName("????????? ?????? ?????? - ?????????????????? ??????")
            @WithMockUser
            void modifyPost_failed_db() throws Exception{
                PostWriteRequest postWriteRequest = new PostWriteRequest("??????????????????", "??????????????????");

                when(postService.modify(any(),any(),any())).thenThrow(new SNSException(ErrorCode.DATABASE_ERROR));

                mockMvc.perform(put("/api/v1/posts/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(postWriteRequest)))
                        .andDo(print())
                        .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
            }
        }


        @Nested
        class CreateTest{
            @Test
            @DisplayName("????????? ?????? ??????")
            @WithMockUser
            void createPost_success() throws Exception {
                PostWriteRequest postWriteRequest = new PostWriteRequest("????????????", "????????????");

                when(postService.create(any(), any())).thenReturn(mock(PostDto.class));

                mockMvc.perform(post("/api/v1/posts")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(postWriteRequest)))
                        .andDo(print())
                        .andExpect(status().isOk());

            }

            @Test
            @DisplayName("????????? ?????? ?????? - ????????? ?????? ?????? ?????? ")
            @WithAnonymousUser
            void createPost_failed() throws Exception {
                PostWriteRequest postWriteRequest = new PostWriteRequest("????????????", "????????????");

                //Controller????????? JWT Filter Exception??? ??????x
//        when(postService.create(any(),any())).thenThrow(new SNSException(ErrorCode.INVALID_PERMISSION));

                mockMvc.perform(post("/api/v1/posts")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(postWriteRequest)))
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }
        }


        @Nested
        class GetTest{
            @Test
            @DisplayName("????????? ?????? ?????? ??????")
            @WithMockUser
            void getOnePost_success() throws Exception {
                PostDto postDto = PostDto.builder()
                        .postId(1L)
                        .title("?????????")
                        .body("?????????")
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
            @DisplayName("????????? ?????? ?????? ??????")
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
    }


}
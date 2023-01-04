package com.sns.mutsasns.controller;

import com.sns.mutsasns.domain.dto.Response;
import com.sns.mutsasns.domain.dto.comment.CommentRequest;
import com.sns.mutsasns.domain.dto.comment.CommentResponse;
import com.sns.mutsasns.domain.dto.posts.PostWriteRequest;
import com.sns.mutsasns.domain.dto.posts.PostWriteResponse;
import com.sns.mutsasns.domain.dto.posts.PostDto;
import com.sns.mutsasns.domain.dto.posts.PostResponse;
import com.sns.mutsasns.service.CommentService;
import com.sns.mutsasns.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/{postsId}/comments")
    public Response<CommentResponse> createComments(@PathVariable Long postsId, @RequestBody CommentRequest commentRequest,Authentication authentication){
        CommentResponse commentResponse = commentService.create(postsId, commentRequest, authentication.getName());
        return Response.success(commentResponse);
    }

    @DeleteMapping("/{id}")
    public Response<PostWriteResponse> deletePost(@PathVariable Long id, Authentication authentication){
        PostDto postDto = postService.delete(id, authentication.getName());
        return Response.success(new PostWriteResponse(postDto.getMessage(),postDto.getPostId()));
    }

    @PutMapping("/{id}")
    public Response<PostWriteResponse> modifyPost(@PathVariable Long id, @RequestBody PostWriteRequest postWriteRequest, Authentication authentication){
        PostDto postDto = postService.modify(id, postWriteRequest, authentication.getName());
        return Response.success(new PostWriteResponse(postDto.getMessage(),postDto.getPostId()));
    }

    @PostMapping("")
    public Response<PostWriteResponse> createPost(@RequestBody PostWriteRequest postWriteRequest, Authentication authentication){
        log.info("isAuthenticated:{},name:{},principle:{},authorities:{}",
                authentication.isAuthenticated(), authentication.getName(), authentication.getPrincipal().toString(),authentication.getAuthorities().toString());
        PostDto postDto = postService.create(postWriteRequest, authentication.getName());
        return Response.success(new PostWriteResponse(postDto.getMessage(),postDto.getPostId()));
    }

    @GetMapping("/{id}")
    public Response<PostResponse> getOnePostById(@PathVariable Long id){
        PostDto onePost = postService.getOnePost(id);
        PostResponse postResponse = PostResponse.builder()
                .id(onePost.getPostId())
                .title(onePost.getTitle())
                .body(onePost.getBody())
                .userName(onePost.getUserName())
                .createdAt(onePost.getCreatedAt())
                .updatedAt(onePost.getUpdatedAt())
                .build();
        return Response.success(postResponse);

    }

    @GetMapping("")
    public Response<Page<PostResponse>> getPostList(@PageableDefault(size = 20)
                                              @SortDefault(sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostDto> allPostsDto = postService.getAllPosts(pageable);
        Page<PostResponse> allpostResponses = allPostsDto.map(PostDto::toResponse);
        return Response.success(allpostResponses);

    }




}

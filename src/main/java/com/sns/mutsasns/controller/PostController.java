package com.sns.mutsasns.controller;

import com.sns.mutsasns.domain.dto.Response;
import com.sns.mutsasns.domain.dto.posts.PostCreateRequest;
import com.sns.mutsasns.domain.dto.posts.PostCreateResponse;
import com.sns.mutsasns.domain.dto.posts.PostDto;
import com.sns.mutsasns.domain.dto.posts.PostResponse;
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

    @PostMapping("")
    public Response<PostCreateResponse> createPost(@RequestBody PostCreateRequest postCreateRequest, Authentication authentication){
        log.info("isAuthenticated:{},name:{},principle:{},authorities:{}",
                authentication.isAuthenticated(), authentication.getName(), authentication.getPrincipal().toString(),authentication.getAuthorities().toString());
        PostDto postDto = postService.create(postCreateRequest, authentication.getName());
        return Response.success(new PostCreateResponse(postDto.getMessage(),postDto.getPostId()));
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

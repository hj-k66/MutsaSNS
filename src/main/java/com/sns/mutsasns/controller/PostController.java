package com.sns.mutsasns.controller;

import com.sns.mutsasns.domain.dto.Response;
import com.sns.mutsasns.domain.dto.posts.PostCreateRequest;
import com.sns.mutsasns.domain.dto.posts.PostCreateResponse;
import com.sns.mutsasns.domain.dto.posts.PostDto;
import com.sns.mutsasns.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}

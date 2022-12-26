package com.sns.mutsasns.service;

import com.sns.mutsasns.domain.dto.posts.PostCreateRequest;
import com.sns.mutsasns.domain.dto.posts.PostDto;
import com.sns.mutsasns.domain.entity.Post;
import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.SNSException;
import com.sns.mutsasns.respository.PostRepository;
import com.sns.mutsasns.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostDto create(PostCreateRequest request, String userName){
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SNSException(ErrorCode.USERNAME_NOT_FOUND));
        Post post = Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .user(user)
                .build();
        Post savedPost = postRepository.save(post);

        return PostDto.builder()
                .message("포스트 등록 완료")
                .postId(savedPost.getId())
                .build();
    }

    public PostDto getOnePost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new SNSException(ErrorCode.POST_NOT_FOUND));
        return PostDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUserName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}

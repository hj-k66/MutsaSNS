package com.sns.mutsasns.service;

import com.sns.mutsasns.domain.dto.posts.PostWriteRequest;
import com.sns.mutsasns.domain.dto.posts.PostDto;
import com.sns.mutsasns.domain.entity.Post;
import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.SNSException;
import com.sns.mutsasns.respository.PostRepository;
import com.sns.mutsasns.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostDto create(PostWriteRequest request, String userName){
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
        return post.toDto();
    }

    public Page<PostDto> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(Post::toDto);
    }

    public PostDto modify(Long postId, PostWriteRequest postWriteRequest, String userName) {
        //포스트 존재 x
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SNSException(ErrorCode.POST_NOT_FOUND));
        //유저 존재 x
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SNSException(ErrorCode.USERNAME_NOT_FOUND));
        //포스트 작성자 != 유저
        if(!Objects.equals(post.getUser().getId(), user.getId())){
            throw new SNSException(ErrorCode.INVALID_PERMISSION);
        }

        post.setTitle(postWriteRequest.getTitle());
        post.setBody(postWriteRequest.getBody());
        Post savedPost = postRepository.saveAndFlush(post);
        log.info("포스트 수정 시간:"+post.getUpdatedAt());
        return PostDto.builder()
                .message("포스트 수정 완료")
                .postId(savedPost.getId())
                .build();
    }

    @Transactional
    public PostDto delete(Long postId, String userName) {
        //포스트 존재 x
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SNSException(ErrorCode.POST_NOT_FOUND));
        //유저 존재 x
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SNSException(ErrorCode.USERNAME_NOT_FOUND));
        //포스트 작성자 != 삭제하려는 유저
        if(!Objects.equals(post.getUser().getId(), user.getId())){
            throw new SNSException(ErrorCode.INVALID_PERMISSION);
        }

        post.delete();
        return PostDto.builder()
                .message("포스트 삭제 완료")
                .postId(post.getId())
                .build();
    }
}

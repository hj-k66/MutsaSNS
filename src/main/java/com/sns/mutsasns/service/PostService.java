package com.sns.mutsasns.service;

import com.sns.mutsasns.domain.dto.posts.PostWriteRequest;
import com.sns.mutsasns.domain.dto.posts.PostDto;
import com.sns.mutsasns.domain.entity.*;

import com.sns.mutsasns.respository.CommentRepository;
import com.sns.mutsasns.respository.LikeRepository;
import com.sns.mutsasns.respository.PostRepository;
import com.sns.mutsasns.respository.UserRepository;
import com.sns.mutsasns.utils.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public PostDto create(PostWriteRequest request, String userName){
        //해당 user 있는지 검증
        User user = Validator.validateUser(userName, userRepository);

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
        //해당 post 있는지 검증
        Post post = Validator.validatePost(id, postRepository);
        return post.toDto();
    }

    public Page<PostDto> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(Post::toDto);
    }

    public PostDto modify(Long postId, PostWriteRequest postWriteRequest, String userName) {
        //포스트 존재 x
        Post post = Validator.validatePost(postId, postRepository);
        //유저 존재 x
        User user = Validator.validateUser(userName, userRepository);
        //포스트 작성자 != 유저
        Validator.validateUserPermission(user.getId(), post.getUser().getId());

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
        Post post = Validator.validatePost(postId, postRepository);
        //유저 존재 x
        User user = Validator.validateUser(userName, userRepository);
        //포스트 작성자 != 삭제하려는 유저
        Validator.validateUserPermission(user.getId(), post.getUser().getId());

        //해당 post의 댓글 모두 삭제
        List<Comment> commentAll = commentRepository.findAllByPostId(postId);
        commentAll .forEach(BaseEntity::delete);
        //해당 post의 좋아요 모두 삭제
        List<Like> LikeAll = likeRepository.findAllByPostId(postId);
        LikeAll.forEach(BaseEntity::delete);
        post.delete();
        return PostDto.builder()
                .message("포스트 삭제 완료")
                .postId(post.getId())
                .build();
    }
}

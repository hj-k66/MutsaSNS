package com.sns.mutsasns.service;

import com.sns.mutsasns.domain.dto.comment.CommentDeleteResponse;
import com.sns.mutsasns.domain.dto.comment.CommentRequest;
import com.sns.mutsasns.domain.dto.comment.CommentResponse;
import com.sns.mutsasns.domain.entity.Comment;
import com.sns.mutsasns.domain.entity.Post;
import com.sns.mutsasns.domain.entity.User;

import com.sns.mutsasns.respository.CommentRepository;
import com.sns.mutsasns.respository.PostRepository;
import com.sns.mutsasns.respository.UserRepository;
import com.sns.mutsasns.utils.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final Validator validator;


    public CommentService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.validator = new Validator(postRepository,userRepository,commentRepository);
    }

    public CommentResponse create(Long postsId, CommentRequest commentRequest, String userName) {
        //해당 Post 있는지 검증
        Post post = validator.validatePost(postsId);
        //해당 user 있는지 검증
        User user = validator.validateUser(userName);

        Comment savedComment = commentRepository.save(commentRequest.toEntity(post, user));
        log.info(commentRequest.getComment());
        return new CommentResponse(savedComment);
    }

    @Transactional
    public CommentResponse modify(Long postsId, Long commentId, CommentRequest commentRequest, String userName) {
        //1. 해당 Post 있는지 검증
        validator.validatePost(postsId);
        //2. 해당 user 있는지 검증
        User loginUser = validator.validateUser(userName);
        //3. 해당 댓글 있는지 검증
        Comment comment = validator.validateComment(commentId);

        //4. 댓글 작성한 회원과 수정하려는 회원이 같은지 검증
        validator.validateUserPermission(loginUser.getId(), comment.getUser().getId());

        comment.changeToComment(commentRequest);
        commentRepository.saveAndFlush(comment);
        CommentResponse commentResponse = new CommentResponse(comment);
        log.info("comment update:"+comment.getUpdatedAt());
        return commentResponse;

    }

    @Transactional
    public CommentDeleteResponse delete(Long postsId, Long commentId, String userName) {
        //1. 해당 Post 있는지 검증
        validator.validatePost(postsId);
        //2. 유저 존재 x
        User loginUser = validator.validateUser(userName);
        //3. 해당 댓글 있는지 검증
        Comment comment = validator.validateComment(commentId);
        //4. 댓글 작성한 회원과 삭제하려는 회원이 같은지 검증 >> comment Domain 로직으로 뺄 수 있지 않을까
        validator.validateUserPermission(loginUser.getId(), comment.getUser().getId());

        comment.delete();
        return new CommentDeleteResponse("댓글 삭제 완료", commentId);
    }

    public Page<CommentResponse> getAllComments(Long postId, Pageable pageable) {
        //해당 Post 있는지 검증
        validator.validatePost(postId);

        Page<Comment> commentPage = commentRepository.findAllByPostId(postId,pageable);
        return commentPage.map(CommentResponse::new);
    }
}

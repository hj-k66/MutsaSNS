package com.sns.mutsasns.service;

import com.sns.mutsasns.domain.dto.comment.CommentDeleteResponse;
import com.sns.mutsasns.domain.dto.comment.CommentRequest;
import com.sns.mutsasns.domain.dto.comment.CommentResponse;
import com.sns.mutsasns.domain.entity.Comment;
import com.sns.mutsasns.domain.entity.Post;
import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.SNSException;
import com.sns.mutsasns.respository.CommentRepository;
import com.sns.mutsasns.respository.PostRepository;
import com.sns.mutsasns.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    public CommentResponse create(Long postsId, CommentRequest commentRequest, String userName) {
        //해당 포스트가 없을 경우
        Post post = postRepository.findById(postsId)
                .orElseThrow(() -> new SNSException(ErrorCode.POST_NOT_FOUND));
        //해당 유저가 없을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SNSException(ErrorCode.USERNAME_NOT_FOUND));
        Comment savedComment = commentRepository.save(commentRequest.toEntity(post, user));
        log.info(commentRequest.getComment());
        return new CommentResponse(savedComment);
    }

    @Transactional
    public CommentResponse modify(Long postsId, Long commentId, CommentRequest commentRequest, String userName) {
        //1. 해당 Post 있는지 검증
        Post post = postRepository.findById(postsId)
                .orElseThrow(() -> new SNSException(ErrorCode.POST_NOT_FOUND));
        //2. 유저 존재 x
        User loginUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SNSException(ErrorCode.USERNAME_NOT_FOUND));
        //3. 해당 댓글 있는지 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new SNSException(ErrorCode.COMMENT_NOT_FOUND));
        //4. 댓글 작성한 회원과 수정하려는 회원이 같은지 검증 >> comment Domain 로직으로 뺄 수 있지 않을까
        if(!Objects.equals(loginUser, comment.getUser())){
            throw new SNSException(ErrorCode.INVALID_PERMISSION);
        }

        comment.changeToComment(commentRequest);
        commentRepository.saveAndFlush(comment);
        CommentResponse commentResponse = new CommentResponse(comment);
        log.info("comment update:"+comment.getUpdatedAt());
        return commentResponse;

    }

    @Transactional
    public CommentDeleteResponse delete(Long postsId, Long commentId, String userName) {
        //1. 해당 Post 있는지 검증
        Post post = postRepository.findById(postsId)
                .orElseThrow(() -> new SNSException(ErrorCode.POST_NOT_FOUND));
        //2. 유저 존재 x
        User loginUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SNSException(ErrorCode.USERNAME_NOT_FOUND));
        //3. 해당 댓글 있는지 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new SNSException(ErrorCode.COMMENT_NOT_FOUND));
        //4. 댓글 작성한 회원과 삭제하려는 회원이 같은지 검증 >> comment Domain 로직으로 뺄 수 있지 않을까
        if(!Objects.equals(loginUser, comment.getUser())){
            throw new SNSException(ErrorCode.INVALID_PERMISSION);
        }

        comment.delete();
        return new CommentDeleteResponse("댓글 삭제 완료", commentId);
    }
}

package com.sns.mutsasns.service;

import com.sns.mutsasns.domain.dto.comment.CommentRequest;
import com.sns.mutsasns.domain.dto.comment.CommentResponse;
import com.sns.mutsasns.domain.entity.Comment;
import com.sns.mutsasns.domain.entity.Post;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.SNSException;
import com.sns.mutsasns.respository.CommentRepository;
import com.sns.mutsasns.respository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    public CommentResponse create(Long postsId, CommentRequest commentRequest) {
        //해당 포스트가 없을 경우
        Post post = postRepository.findById(postsId)
                .orElseThrow(() -> new SNSException(ErrorCode.POST_NOT_FOUND));

        Comment savedComment = commentRepository.save(commentRequest.toEntity(post));
        log.info(commentRequest.getComment());
        return new CommentResponse(savedComment);
    }
}

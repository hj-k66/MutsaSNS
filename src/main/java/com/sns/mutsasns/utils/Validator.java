package com.sns.mutsasns.utils;

import com.sns.mutsasns.domain.entity.Comment;
import com.sns.mutsasns.domain.entity.Post;
import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.SNSException;
import com.sns.mutsasns.respository.CommentRepository;
import com.sns.mutsasns.respository.PostRepository;
import com.sns.mutsasns.respository.UserRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
@Component
public class Validator {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    @Builder
    public Validator(PostRepository postRepository,UserRepository userRepository,CommentRepository commentRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    //해당 Post 있는지 검증
    public Post validatePost(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new SNSException(ErrorCode.POST_NOT_FOUND));
    }
    //해당 user 있는지 검증
    public User validateUser(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new SNSException(ErrorCode.USERNAME_NOT_FOUND));
    }
    //해당 Comment 있는지 검증
    public Comment validateComment(Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new SNSException(ErrorCode.COMMENT_NOT_FOUND));
    }

    //user Permission 검증
    //ex. 댓글 작성한 회원(authorUserId)과 수정하려는 회원(loginUserId)이 같은지 검증
    public void validateUserPermission(Long loginUserId, Long authorUserId){
        if(!Objects.equals(loginUserId, authorUserId)){
            throw new SNSException(ErrorCode.INVALID_PERMISSION);
        }
    }

}

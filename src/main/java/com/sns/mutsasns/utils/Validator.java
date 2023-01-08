package com.sns.mutsasns.utils;

import com.sns.mutsasns.domain.entity.Comment;
import com.sns.mutsasns.domain.entity.Post;
import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.SNSException;
import com.sns.mutsasns.respository.CommentRepository;
import com.sns.mutsasns.respository.PostRepository;
import com.sns.mutsasns.respository.UserRepository;

import java.util.Objects;


public class Validator {
    //해당 Post 있는지 검증
    public static Post validatePost(Long postId, PostRepository postRepository){
        return postRepository.findById(postId)
                .orElseThrow(() -> new SNSException(ErrorCode.POST_NOT_FOUND));
    }
    //해당 user 있는지 검증
    public static User validateUser(String userName, UserRepository userRepository){
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new SNSException(ErrorCode.USERNAME_NOT_FOUND));
    }
    //해당 Comment 있는지 검증
    public static Comment validateComment(Long commentId, CommentRepository commentRepository){
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new SNSException(ErrorCode.COMMENT_NOT_FOUND));
    }

    //user Permission 검증
    //ex. 댓글 작성한 회원(authorUserId)과 수정하려는 회원(loginUserId)이 같은지 검증
    public static void validateUserPermission(Long loginUserId, Long authorUserId){
        if(!Objects.equals(loginUserId, authorUserId)){
            throw new SNSException(ErrorCode.INVALID_PERMISSION);
        }
    }

}

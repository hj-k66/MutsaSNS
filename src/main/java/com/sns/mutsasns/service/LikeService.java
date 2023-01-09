package com.sns.mutsasns.service;

import com.sns.mutsasns.domain.dto.alarm.AlarmType;
import com.sns.mutsasns.domain.dto.like.LikeCountResponse;
import com.sns.mutsasns.domain.dto.like.LikeResponse;
import com.sns.mutsasns.domain.entity.Alarm;
import com.sns.mutsasns.domain.entity.Like;
import com.sns.mutsasns.domain.entity.Post;
import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.respository.AlarmRepository;
import com.sns.mutsasns.respository.LikeRepository;
import com.sns.mutsasns.respository.PostRepository;
import com.sns.mutsasns.respository.UserRepository;
import com.sns.mutsasns.utils.Validator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class LikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final AlarmRepository alarmRepository;
    private final Validator validator;

    public LikeService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository, AlarmRepository alarmRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.alarmRepository = alarmRepository;
        this.validator = Validator.builder()
                .postRepository(postRepository)
                .userRepository(userRepository)
                .build();
    }
    @Transactional
    public LikeResponse createLike(Long postId, String userName) {
        //해당 포스트 있는지 검증
        Post post = validator.validatePost(postId);
        //해당 유저 있는지 검증
        User user = validator.validateUser(userName);

        //해당 userId와 postId로 조회했을 때 존재하면 delete_at 기록
        String message;
        Optional<Like> opLike = likeRepository.findByUserIdAndPostId(user.getId(), postId);
        if(opLike.isPresent()){
            opLike.get().delete();
            message = "좋아요가 취소되었습니다.";
        }else{ //없으면 새롭게 추가
            likeRepository.save(Like.builder()
                .post(post)
                .user(user)
                .build());
            message = userName + "님이 좋아요를 눌렀습니다.";
            //해당 post에 알림 보내기
            alarmRepository.save(Alarm.makeAlarm(AlarmType.NEW_LIKE_ON_POST,user.getId(),post));
        }
        return new LikeResponse(message);
    }

    public LikeCountResponse getLikeCount(Long postId) {
        //해당 포스트 있는지 검증
        validator.validatePost(postId);

        int likeCount = likeRepository.countByPostId(postId);
        return new LikeCountResponse(likeCount);
    }
}

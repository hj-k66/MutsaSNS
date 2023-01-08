package com.sns.mutsasns.service;

import com.sns.mutsasns.domain.dto.like.LikeResponse;
import com.sns.mutsasns.respository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    public LikeResponse createLike(Long postId, String userName) {
        //해당 포스트 있는지 검증
        //해당 유저 있는지 검증
        //해당 userId로 조회했을 때 존재하면 delete_at 기록
        //없으면 새롭게 추가
        return null;
    }
}

package com.sns.mutsasns.respository;

import com.sns.mutsasns.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByPostId(Long postId);
}

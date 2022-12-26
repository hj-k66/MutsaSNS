package com.sns.mutsasns.respository;

import com.sns.mutsasns.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

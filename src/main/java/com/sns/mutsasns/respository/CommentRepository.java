package com.sns.mutsasns.respository;

import com.sns.mutsasns.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

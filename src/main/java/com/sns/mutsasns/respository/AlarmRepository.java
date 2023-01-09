package com.sns.mutsasns.respository;

import com.sns.mutsasns.domain.entity.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository  extends JpaRepository<Alarm, Long> {
    Page<Alarm> findAllByUserId(Long userId,Pageable pageable);

}

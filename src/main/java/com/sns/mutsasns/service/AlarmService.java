package com.sns.mutsasns.service;

import com.sns.mutsasns.domain.dto.alarm.AlarmResponse;
import com.sns.mutsasns.domain.entity.Alarm;
import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.respository.AlarmRepository;
import com.sns.mutsasns.respository.UserRepository;
import com.sns.mutsasns.utils.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final Validator validator;

    public AlarmService(AlarmRepository alarmRepository, UserRepository userRepository) {
        this.alarmRepository = alarmRepository;
        this.userRepository = userRepository;
        this.validator = Validator.builder()
                .userRepository(userRepository)
                .build();
    }

    public Page<AlarmResponse> getAlarmList(String loginUserName, Pageable pageable) {
        //해당 user 있는지 검증
        User user = validator.validateUser(loginUserName);
        //로그인한 유저의 모든 글에 대한 알람 조회
        Page<Alarm> alarms = alarmRepository.findAllByUserId(user.getId(), pageable);
        return alarms.map(AlarmResponse::new);
    }
}

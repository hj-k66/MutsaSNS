package com.sns.mutsasns.domain.dto.alarm;

import com.sns.mutsasns.domain.entity.Alarm;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AlarmResponse {
    private Long id;
    private AlarmType alarmType;
    private Long fromUserId;
    private Long targetId;
    private String text;
    private LocalDateTime createdAt;

    //entity -> dto
    public AlarmResponse(Alarm alarm){
        this.id = alarm.getId();
        this.alarmType = alarm.getAlarmType();
        this.fromUserId = alarm.getFromUserId();
        this.targetId = alarm.getTargetId();
        this.text = alarm.getText();
        this.createdAt = alarm.getCreatedAt();

    }
}

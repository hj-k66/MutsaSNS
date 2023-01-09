package com.sns.mutsasns.domain.entity;

import com.sns.mutsasns.domain.dto.alarm.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alarm extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String alarmType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long fromUserId; //알림을 발생 시킨 userId ex) 댓글 쓴 사람, 좋아요 누른 사람
    private Long targetId; //알림이 발생된 post id
    private String text;

    public static Alarm makeAlarm(AlarmType alarmType, Long fromUserId, Post post){
        return Alarm.builder()
                .alarmType(alarmType.name())
                .user(post.getUser())
                .fromUserId(fromUserId)
                .targetId(post.getId())
                .text(alarmType.getText())
                .build();
    }
}

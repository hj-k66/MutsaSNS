package com.sns.mutsasns.controller;

import com.sns.mutsasns.domain.dto.Response;
import com.sns.mutsasns.domain.dto.alarm.AlarmResponse;
import com.sns.mutsasns.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping("")
    public Response<Page<AlarmResponse>> getAlarmList(Authentication authentication,@PageableDefault(size = 20)
                                @SortDefault(sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable){
        Page<AlarmResponse> alarmResponses = alarmService.getAlarmList(authentication.getName(), pageable);
        return Response.success(alarmResponses);
    }
}

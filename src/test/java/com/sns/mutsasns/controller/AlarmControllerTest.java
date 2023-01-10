package com.sns.mutsasns.controller;

import com.sns.mutsasns.service.AlarmService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlarmController.class)
class AlarmControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AlarmService alarmService;

    @Test
    @DisplayName("알림 리스트 조회 성공")
    @WithMockUser
    void get_alarm_list_success() throws Exception {
        when(alarmService.getAlarmList(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/alarms")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "createdAt,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(alarmService).getAlarmList(any(), pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        System.out.println(pageable.toString());


        Assertions.assertEquals(0, pageable.getPageNumber());
        Assertions.assertEquals(20, pageable.getPageSize());
        Assertions.assertEquals(Sort.by("createdAt", "desc"), pageable.withSort(Sort.by("createdAt", "desc")).getSort());

    }

    @Test
    @DisplayName("알림 리스트 조회 실패 - 로그인 하지 않은 경우")
    @WithAnonymousUser
    void get_alarm_list_fail_no_login() throws Exception {
        when(alarmService.getAlarmList(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/alarms"))
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }
}

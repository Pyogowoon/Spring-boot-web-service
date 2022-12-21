package com.pyo.web.springboot.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

 //스프링 시큐리티는 권한코드 앞에 항상 ROLE 이 있어야함 그래서 키값 ROLE
        GUEST("ROLE_GUEST" , "손님"),
        USER("ROLE_USER" , "일반 사용자");

        private final String key;
        private final String title;
    }


package com.example.sample1.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

public class CustomSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    private final SessionRegistry sessionRegistry;

    public CustomSessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        // 사용자 세션 중복 확인 후, 기존 세션 종료
        String username = authentication.getName();
        for (SessionInformation session : sessionRegistry.getAllSessions(authentication.getPrincipal(), false)) {
            // 세션 만료 처리
            session.expireNow();
        }

        // 새로운 로그인 세션 시작
        sessionRegistry.registerNewSession(request.getSession().getId(), authentication.getPrincipal());
    }
}
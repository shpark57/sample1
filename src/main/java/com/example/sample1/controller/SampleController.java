package com.example.sample1.controller;

import com.example.sample1.service.UsrInfoMgntService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SampleController {

    private final UsrInfoMgntService usrInfoMgntService;

    public SampleController(UsrInfoMgntService usrInfoMgntService) {
        this.usrInfoMgntService = usrInfoMgntService;
    }

    @GetMapping("/")
    public String home() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.println("Authenticated user: " + authentication.getName());


        return "home";  // home.html을 반환
    }

    @GetMapping("/login")
    public String login() {
        return "login";  // login.html을 반환
    }

    @GetMapping("/join")
    public String join() {
        return "join";  // login.html을 반환
    }

    @GetMapping("/sessionTimeout")
    public String sessionTimeout() {
        return "sessionTimeout";  // 세션만료
    }

    @PostMapping("/join")
    public String join(@RequestParam String usrId, @RequestParam String scrtNo, @RequestParam String tenantId) {
        // 회원가입 처리
        usrInfoMgntService.registerUsrInfoMgnt(usrId, scrtNo, tenantId);
        return "redirect:/login";  // 회원가입 후 로그인 페이지로 리다이렉트
    }
}

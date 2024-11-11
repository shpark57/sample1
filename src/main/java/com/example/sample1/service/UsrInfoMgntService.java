package com.example.sample1.service;

import com.example.sample1.config.CustomUser;
import com.example.sample1.entity.UsrInfoMgnt;
import com.example.sample1.repository.UsrInfoMgntRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsrInfoMgntService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UsrInfoMgntRepository usrInfoMgntRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final HttpServletRequest request;

    @Autowired
    @Lazy
    public UsrInfoMgntService(UsrInfoMgntRepository usrInfoMgntRepository, BCryptPasswordEncoder  passwordEncoder, HttpServletRequest request) {
        this.usrInfoMgntRepository = usrInfoMgntRepository;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
    }

    public void registerUsrInfoMgnt(String usrId , String scrtNo , String tenantId){
        UsrInfoMgnt user = new UsrInfoMgnt();
        user.setUsrId(usrId);
        user.setScrtNo(passwordEncoder.encode(scrtNo) );  // 실제로는 비밀번호 암호화가 필요합니다.
        user.setTenantId(tenantId);
        user.setUsrGrd("900");  // 운영자 권한 900

        usrInfoMgntRepository.save(user);  // 데이터베이스에 사용자 정보 저장
    }

    public UserDetails loadUserByUsername(String usrId) throws UsernameNotFoundException {
        String tenantId = getTenantIdFromRequest();  // 요청에서 tenantId를 파라미터로 받아오는 메서드
        UsrInfoMgnt usrInfoMgnt = usrInfoMgntRepository.findByUsrIdAndTenantId(usrId , tenantId);  // 사용자 조회

        if(usrInfoMgnt == null){
            throw new UsernameNotFoundException("User not found");
        }
        // usrGrd 값으로 역할을 ROLE_로 변환
        String role = "ROLE_" + usrInfoMgnt.getUsrGrd();  // 예: "900" -> "ROLE_900"


        // 비밀번호 비교
        if (!passwordEncoder.matches(request.getParameter("scrtNo"), usrInfoMgnt.getScrtNo())) {
            throw new UsernameNotFoundException("Password does not match");
        }

        return new CustomUser(
            usrInfoMgnt.getUsrId(),
            usrInfoMgnt.getScrtNo(),  // 비밀번호
            Collections.singletonList(new SimpleGrantedAuthority(role)),  // 권한
            tenantId  // tenantId 포함
        );
    }


    // 요청에서 tenantId를 파라미터로 받아오는 메서드
    private String getTenantIdFromRequest() {
        // 예시: 로그인 폼에서 tenantId가 전달되는 경우
        String tenantId = request.getParameter("tenantId");
        String usrId = request.getParameter("usrId");
if (usrId == null) {
    System.out.println("usrId is null");  // 로그 출력
}
        if (tenantId == null) {
            throw new IllegalArgumentException("tenantId is required");
        }
        return tenantId;
    }
}

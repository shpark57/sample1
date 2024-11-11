package com.example.sample1.config;

import com.example.sample1.service.UsrInfoMgntService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final UsrInfoMgntService usrInfoMgntService;

    // 생성자 주입
    public SecurityConfig(UsrInfoMgntService usrInfoMgntService) {
        this.usrInfoMgntService = usrInfoMgntService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 세션이 필요한 경우에만 생성
                .maximumSessions(1)  // 최대 1개의 세션만 허용 (중복 로그인 방지)
                .maxSessionsPreventsLogin(false)  // 중복 로그인 시, 기존로그인 추방
                .expiredUrl("/sessionTimeout")  // 세션 만료 시 /sessionTimeout으로 리디렉션
                .sessionRegistry(sessionRegistry())  // 세션 레지스트리 설정
            )
            .authorizeRequests(auth -> auth
                .requestMatchers("/join", "/login", "/sessionTimeout").permitAll()  // 로그인과 회원가입 페이지는 인증 없이 접근
                .requestMatchers("/**").hasRole("900")
                .anyRequest().authenticated()  // 나머지 경로는 인증 필요
            )
            .formLogin(form -> form
                .loginPage("/login")  // 로그인 페이지 경로
                .usernameParameter("usrId")  // 사용자명 파라미터 이름
                .passwordParameter("scrtNo")  // 비밀번호 파라미터 이름
                .defaultSuccessUrl("/",true)
                .permitAll()  // 로그인 페이지는 인증 없이 접근 가능
            )
            .logout(logout -> logout
                .logoutUrl("/logout")  // 로그아웃 URL
                .invalidateHttpSession(true)  // 로그아웃 시 세션 무효화
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/join", "/sessionTimeout")  // /join 경로에 대해 CSRF 보호 비활성화
            )
        ;


        return http.build();
    }




    @Bean
    public BCryptPasswordEncoder  passwordEncoder() {
        return new BCryptPasswordEncoder();  // 비밀번호 인코딩 설정
    }



    // AuthenticationManager 빈을 등록하는 메서드
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(usrInfoMgntService)  // 사용자 서비스 설정
            .passwordEncoder(passwordEncoder());  // 비밀번호 인코딩 설정
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}

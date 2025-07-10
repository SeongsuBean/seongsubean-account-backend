package com.oopsw.accountservice.config;

import com.oopsw.accountservice.account.jpa.AccountRepository;
import com.oopsw.accountservice.auth.AccountOauth2UserService;
import com.oopsw.accountservice.auth.CustomAuthenticationEntryPoint;
import com.oopsw.accountservice.jwt.JwtAuthorizationFilter;
import com.oopsw.accountservice.jwt.JwtBasicAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  @Autowired
  private CorsFilter corsFilter;
  private final AccountOauth2UserService accountOauth2UserService;

  // 1. AuthenticationManager 등록
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  // 2. SecurityFilterChain 설정
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                 AuthenticationManager authenticationManager,
                                                  AccountRepository accountRepository,
                                                  CustomAuthenticationEntryPoint entryPoint) throws Exception {

    http.csrf(csrf -> csrf.disable()).headers(headers -> headers
        .frameOptions(frameOptions -> frameOptions.sameOrigin()) //이걸 추가해야 H2-CONSOLE을 쓸수있음
    );
    http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.formLogin(form -> form.disable());
    // 기본 HTTP 인증 제거
    http.httpBasic(basic -> basic.disable());
    http.addFilter(corsFilter);
    http.addFilter(new JwtAuthorizationFilter(authenticationManager));
    http.addFilter(new JwtBasicAuthenticationFilter(authenticationManager, accountRepository));
    http.authorizeHttpRequests(auth ->
            auth.requestMatchers("/api/v1/user/**").authenticated() //로그인 하면 모두
                    .anyRequest().permitAll() );

    http.exceptionHandling(eh -> eh.authenticationEntryPoint(entryPoint));

    http.oauth2Login(oauth2 -> oauth2
            .loginPage("/account/login-view.html")
            .defaultSuccessUrl("/", true)
            .userInfoEndpoint(userInfo -> userInfo
                    .userService(accountOauth2UserService)
            )
    );

    return http.build();
  }
}

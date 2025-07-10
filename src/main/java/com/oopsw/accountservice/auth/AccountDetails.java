package com.oopsw.accountservice.auth;

import com.oopsw.accountservice.account.jpa.UserEntity;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class AccountDetails implements UserDetails, OAuth2User {
  private final UserEntity user;
  private Map<String, Object> attributes;

  public AccountDetails(UserEntity user) {this.user = user;}

  public AccountDetails(UserEntity user, Map<String, Object> attributes) {
    this.user = user;
    this.attributes = attributes;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes != null ? attributes : Map.of();
  }

  @Override
  public String getName() {
    return user.getNickname();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String role = user.getRole(); // 예: "CUSTOMER", "OWNER"
    if (role == null || role.isBlank()) {
      throw new IllegalStateException("Role is not set for account: " + user.getEmail());
    }
    return List.of(new SimpleGrantedAuthority("ROLE_" + role)); // ✅ ROLE_ 접두사 붙이기
  }

  @Override
  public String getPassword() {
    return user.getEncryptedPwd();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public UserEntity getUser() {
    return this.user;
  }

  public String getImage(){
    return user.getImage();
  }
}

package com.oopsw.accountservice.account.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<UserEntity, String> {
  UserEntity findByEmail(String email);
  boolean existsByNickname(String nickname);
  boolean existsByEmail(String email);
  void deleteByEmail(String email);
}

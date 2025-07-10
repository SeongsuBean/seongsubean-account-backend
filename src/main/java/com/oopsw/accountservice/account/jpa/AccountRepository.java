package com.oopsw.accountservice.account.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<UserEntity, String> {
  UserEntity findByEmail(String email);
  Boolean existsByNickname(String nickname);
}

package com.oopsw.accountservice.repository;

import com.oopsw.accountservice.account.jpa.AccountRepository;
import com.oopsw.accountservice.account.jpa.UserEntity;
import com.oopsw.accountservice.config.EncoderConfig;
import java.time.LocalDate;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DataJpaTest
@Log4j2
@Import(EncoderConfig.class)
public class AccountRepositoryTest {
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Test
  public void addMenu_Success() {
    UserEntity user = new UserEntity();
    user.setEncryptedPwd(bCryptPasswordEncoder.encode("1234"));
    user.setPhoneNumber("010-2323-2323");
    user.setEmail("test@test.com");
    user.setNickname("test");
    user.setRole("CUSTOMER");
    user.setBirthDate(LocalDate.of(2000,2,20));
    user.setOauth(false);
    accountRepository.save(user);
    UserEntity found = accountRepository.findById("test@test.com").get();
    Assertions.assertWith(found.getNickname()).isEqualTo("test");
  }
}

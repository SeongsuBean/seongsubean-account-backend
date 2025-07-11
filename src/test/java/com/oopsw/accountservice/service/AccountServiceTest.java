package com.oopsw.accountservice.service;

import com.oopsw.accountservice.account.dto.UserDTO;
import com.oopsw.accountservice.account.jpa.AccountRepository;
import com.oopsw.accountservice.account.jpa.UserEntity;
import com.oopsw.accountservice.account.service.AccountService;
import com.oopsw.accountservice.account.vo.RequestEditProfile;
import java.time.LocalDate;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
public class AccountServiceTest {
  @Autowired
  private AccountService accountService;
  @Autowired
  private AccountRepository accountRepository;

  @BeforeEach
  public void setUp() {
    UserDTO user = new UserDTO();
    user.setPassword("1234");
    user.setPhoneNumber("010-2323-2323");
    user.setEmail("test@test.com");
    user.setNickname("test");
    user.setBirthDate(LocalDate.of(2000,2,20));

    accountService.addAccount(user);
  }

  @Test
  public void addUser_Success() {
    UserDTO user = new UserDTO();
    user.setPassword("1234");
    user.setPhoneNumber("010-2323-2323");
    user.setEmail("test@test.com");
    user.setNickname("test");
    user.setBirthDate(LocalDate.of(2000,2,20));

    Boolean result = accountService.addAccount(user);

    assertThat(result).isTrue();
  }

  @Test
  public void deleteUser_Success() {
    accountService.deleteAccount("test@test.com");
    log.info(accountService.existsEmail("test@test.com"));
  }

  @Test
  public void setUserInfo_Success() {
    UserEntity user = accountRepository.findByEmail("test@test.com");
    UserDTO userDTO = new ModelMapper().map(user, UserDTO.class);

    RequestEditProfile requestEditProfile = RequestEditProfile.builder().newPassword("12345").build();

    accountService.setUserInfo(requestEditProfile, userDTO);

    user = accountRepository.findByEmail("test@test.com");
    log.info(user);
  }
}

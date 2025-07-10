package com.oopsw.accountservice.service;

import com.oopsw.accountservice.account.dto.UserDTO;
import com.oopsw.accountservice.account.service.AccountService;
import java.time.LocalDate;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
public class AccountServiceTest {
  @Autowired
  private AccountService accountService;

  @Test
  public void addUser_Success() {
    UserDTO user = new UserDTO();
    user.setPassword("1234");
    user.setPhoneNumber("010-2323-2323");
    user.setEmail("test@test.com");
    user.setNickname("test");
    user.setBirthDate(LocalDate.of(2000,2,20));

    Boolean result = accountService.addUser(user);

    assertThat(result).isTrue();
  }
}

package com.oopsw.accountservice.account.vo;

import java.time.LocalDate;
import lombok.Data;

@Data
public class RequestAddUser {
  private String email;
  private String nickname;
  private String password;
  private String phoneNumber;
  private LocalDate birthDate;
}

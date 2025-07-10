package com.oopsw.accountservice.account.vo;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserManagement {
  private String email;
  private String nickName;
  private String role;
}

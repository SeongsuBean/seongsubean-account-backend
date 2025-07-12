package com.oopsw.accountservice.account.vo;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ResponseUser {
  private String nickname;
  private String email;
  private String role;
  private LocalDate joinDate;
  private boolean isReport;
}

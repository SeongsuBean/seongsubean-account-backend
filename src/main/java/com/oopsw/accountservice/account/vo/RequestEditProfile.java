package com.oopsw.accountservice.account.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestEditProfile {
  private String newNickname;
  private String newPassword;
  private String password;
}

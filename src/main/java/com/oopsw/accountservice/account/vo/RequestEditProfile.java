package com.oopsw.accountservice.account.vo;

import lombok.Data;

@Data
public class RequestEditProfile {
  private String nickname;
  private String password;
}

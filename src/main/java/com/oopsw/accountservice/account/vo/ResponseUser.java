package com.oopsw.accountservice.account.vo;

import lombok.Data;

@Data
public class ResponseUser {
  private String nickname;
  private String email;
  private String role;
  private String image;
}

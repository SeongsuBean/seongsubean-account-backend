package com.oopsw.accountservice.account.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"password", "encryptedPwd"})
public class UserDTO {
  private String email;
  private String nickname;
  private String password;
  private String encryptedPwd;
  private String phoneNumber;
  private String image;
  private String role;
  private boolean isOauth;
  private boolean isReport;
  private LocalDate birthDate;
  private LocalDate joinDate;
}

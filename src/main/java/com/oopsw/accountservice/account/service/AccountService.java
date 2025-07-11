package com.oopsw.accountservice.account.service;

import com.oopsw.accountservice.account.dto.UserDTO;
import com.oopsw.accountservice.account.vo.RequestEditProfile;

public interface AccountService {
  boolean addAccount(UserDTO userDTO);
  boolean existsNickname(String nickname);
  boolean existsEmail(String email);
  void deleteAccount(String email);
  void setUserInfo(RequestEditProfile user, UserDTO userDTO);
}

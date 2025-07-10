package com.oopsw.accountservice.account.service;

import com.oopsw.accountservice.account.dto.UserDTO;

public interface AccountService {
  boolean addUser(UserDTO userDTO);
  boolean existsNickname(String nickname);
  boolean existsEmail(String email);
}

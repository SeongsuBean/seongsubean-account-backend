package com.oopsw.accountservice.account.service;

import com.oopsw.accountservice.account.dto.UserDTO;
import com.oopsw.accountservice.account.vo.RequestEditProfile;
import java.util.List;

public interface AccountService {
  boolean addAccount(UserDTO userDTO);
  boolean existsNickname(String nickname);
  boolean existsEmail(String email);
  void deleteAccount(String email);
  void setUserInfo(RequestEditProfile user, UserDTO userDTO);
  void setUserImage(UserDTO userDTO);
  List<UserDTO> getAllUsers();
  UserDTO getUserByEmail(String email);
  void setUserRole(UserDTO userDTO);
}

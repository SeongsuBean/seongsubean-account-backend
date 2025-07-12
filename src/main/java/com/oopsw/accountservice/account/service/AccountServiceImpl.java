package com.oopsw.accountservice.account.service;

import com.oopsw.accountservice.account.dto.UserDTO;
import com.oopsw.accountservice.account.jpa.AccountRepository;
import com.oopsw.accountservice.account.jpa.UserEntity;
import com.oopsw.accountservice.account.vo.RequestEditProfile;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final AccountRepository accountRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  @Override
  public boolean addAccount(UserDTO userDTO) {
    boolean result = false;
    userDTO.setEncryptedPwd(bCryptPasswordEncoder.encode(userDTO.getPassword()));
    userDTO.setOauth(false);
    userDTO.setRole("CUSTOMER");
    try{
      accountRepository.save(new ModelMapper().map(userDTO, UserEntity.class));
      result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public boolean existsNickname(String nickname) {
    return accountRepository.existsByNickname(nickname);
  }

  @Override
  public boolean existsEmail(String email) {
    return accountRepository.existsByEmail(email);
  }

  @Transactional
  @Override
  public void deleteAccount(String email) {
    accountRepository.deleteByEmail(email);
  }


  @Override
  public void setUserInfo(RequestEditProfile user, UserDTO userDTO) {
    if (user.getNewPassword() != null && !user.getNewPassword().isBlank()) userDTO.setEncryptedPwd(bCryptPasswordEncoder.encode(user.getNewPassword()));
    if (user.getNewNickname() != null && !user.getNewNickname().isBlank()) userDTO.setNickname(user.getNewNickname());
    accountRepository.save(new ModelMapper().map(userDTO, UserEntity.class));
  }

  @Override
  public void setUserImage(UserDTO userDTO) {
    accountRepository.save(new ModelMapper().map(userDTO, UserEntity.class));
  }
}

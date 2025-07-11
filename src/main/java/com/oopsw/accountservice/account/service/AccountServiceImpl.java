package com.oopsw.accountservice.account.service;

import com.oopsw.accountservice.account.dto.UserDTO;
import com.oopsw.accountservice.account.jpa.AccountRepository;
import com.oopsw.accountservice.account.jpa.UserEntity;
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
}

package com.oopsw.accountservice.account.controller;

import com.oopsw.accountservice.account.dto.UserDTO;
import com.oopsw.accountservice.account.service.AccountService;
import com.oopsw.accountservice.account.vo.RequestAddUser;
import com.oopsw.accountservice.auth.AccountDetails;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountRestController {
  private final AccountService accountService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  //회원가입
  @PostMapping
  public ResponseEntity<Map<String, Boolean>> joinAccount(@RequestBody RequestAddUser user) {
    return ResponseEntity.ok().body(Map.of("result", accountService.addAccount(new ModelMapper().map(user, UserDTO.class))));
  }

  //이메일 중복검사
  @GetMapping("/exists/email/{email}")
  public ResponseEntity<Map<String, Boolean>> isExistsEmail(@PathVariable String email) {
    return ResponseEntity.ok(Map.of("result", accountService.existsEmail(email)));
  }

  //닉네임 중복검사
  @GetMapping("/exists/nickname/{nickname}")
  public ResponseEntity<Map<String, Boolean>> isExistsNickname(@PathVariable String nickname) {
    return ResponseEntity.ok(Map.of("result", accountService.existsNickname(nickname)));
  }

  //회원 탈퇴
  @DeleteMapping
  public ResponseEntity<Map<String, String>> deleteAccount(Authentication auth) {
    AccountDetails user = (AccountDetails) auth.getPrincipal();
    accountService.deleteAccount(user.getUsername());
    return ResponseEntity.ok(Map.of("message", "회원 탈퇴를 완료하였습니다."));
  }
}

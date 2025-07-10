package com.oopsw.accountservice.account.controller;

import com.oopsw.accountservice.account.dto.UserDTO;
import com.oopsw.accountservice.account.service.AccountService;
import com.oopsw.accountservice.account.vo.RequestAddUser;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

  @PostMapping
  public ResponseEntity<Map<String, Boolean>> joinAccount(@RequestBody RequestAddUser user) {
    return ResponseEntity.ok().body(Map.of("result", accountService.addUser(new ModelMapper().map(user, UserDTO.class))));
  }

  @PostMapping("/exists/email/{email}")
  public Map<String, Boolean> isExistsEmail(@PathVariable String email) {
    return Map.of("result", accountService.existsEmail(email));
  }

  @PostMapping("/exists/nickname/{nickname}")
  public Map<String, Boolean> isExistsNickname(@PathVariable String nickname) {
    return Map.of("result", accountService.existsNickname(nickname));
  }
}

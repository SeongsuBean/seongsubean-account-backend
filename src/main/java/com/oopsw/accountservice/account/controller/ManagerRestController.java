package com.oopsw.accountservice.account.controller;

import com.oopsw.accountservice.account.service.AccountService;
import com.oopsw.accountservice.account.vo.ResponseUser;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager")
public class ManagerRestController {
  private final AccountService accountService;

  //모든 회원 정보
  @GetMapping("/users")
  public ResponseEntity<List<ResponseUser>> getAllUsers(){
    List<ResponseUser> userList = new ArrayList<>();
    accountService.getAllUsers().forEach(userDTO -> {userList.add(new ModelMapper().map(userDTO, ResponseUser.class));});
    return ResponseEntity.ok(userList);
  }

  //회원 검색 - 이메일
  @GetMapping("/users/{email}")
  public ResponseEntity<ResponseUser> getUser(@PathVariable String email){
    return ResponseEntity.ok(new ModelMapper().map(accountService.getUserByEmail(email), ResponseUser.class));
  }
}

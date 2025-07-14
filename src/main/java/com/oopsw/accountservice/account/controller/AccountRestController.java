package com.oopsw.accountservice.account.controller;

import com.oopsw.accountservice.account.dto.UserDTO;
import com.oopsw.accountservice.account.service.AccountService;
import com.oopsw.accountservice.account.vo.RequestAddUser;
import com.oopsw.accountservice.account.vo.RequestEditProfile;
import com.oopsw.accountservice.account.vo.ResponseProfile;
import com.oopsw.accountservice.auth.AccountDetails;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountRestController {

  private final AccountService accountService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @PutMapping("/role/{role}")
  public ResponseEntity<Map<String, String>> setRole(@PathVariable String role, Authentication auth){
    if ("owner".equalsIgnoreCase(role)) {role = "OWNER";}
    else if ("customer".equalsIgnoreCase(role)) {role = "CUSTOMER";}
    else {return ResponseEntity.badRequest().body(Map.of("message", role + " 권한은 없습니다."));}

    AccountDetails accountDetails = (AccountDetails) auth.getPrincipal();
    UserDTO userDTO = new ModelMapper().map(accountDetails.getUser(), UserDTO.class);

    userDTO.setRole(role);
    accountService.setUserRole(userDTO);

    return ResponseEntity.ok(Map.of("message", "권한이 " + role + "로 변경되었습니다."));
  }

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

  //회원정보 수정 전 비밀번호 확인
  @PostMapping("/profile")
  public ResponseEntity<Map<String, Boolean>> checkPw(Authentication auth,
      @RequestBody RequestEditProfile requestEditProfile) {
    System.out.println(requestEditProfile.getPassword());
    AccountDetails accountDetails = (AccountDetails) auth.getPrincipal();
    return ResponseEntity.ok(Map.of("result",
        bCryptPasswordEncoder.matches(requestEditProfile.getPassword(),
            accountDetails.getUser().getEncryptedPwd())));
  }

  //회원 정보 가져오기
  @GetMapping("/profile")
  public ResponseEntity<ResponseProfile> getProfile(Authentication auth) {
    AccountDetails accountDetails = (AccountDetails) auth.getPrincipal();
    ResponseProfile profile = new ModelMapper().map(accountDetails.getUser(),
        ResponseProfile.class);
    return ResponseEntity.ok(profile);
  }

  //회원 정보 수정
  @PutMapping("/profile")
  public ResponseEntity<Map<String, String>> editProfile(@RequestBody RequestEditProfile user,
      Authentication auth) {
    AccountDetails accountDetails = (AccountDetails) auth.getPrincipal();
    UserDTO userDTO = new ModelMapper().map(accountDetails.getUser(), UserDTO.class);

    accountService.setUserInfo(user, userDTO);
    return ResponseEntity.ok(Map.of("message", "정보를 수정하였습니다."));
  }

  //회원 이미지 수정
  @PutMapping("/profile/image")
  public ResponseEntity<Map<String, String>> setImage(@RequestBody String imageFile,
      Authentication auth){
    AccountDetails accountDetails = (AccountDetails) auth.getPrincipal();
    UserDTO userDTO = new ModelMapper().map(accountDetails.getUser(), UserDTO.class);

    String safeFilename = imageFile.replaceAll("\\s+", "_");

    userDTO.setImage(safeFilename);
    accountService.setUserImage(userDTO);

    return ResponseEntity.ok(Map.of("message", "업로드 성공"));
  }
}

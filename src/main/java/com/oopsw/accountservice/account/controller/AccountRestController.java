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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountRestController {

  private final AccountService accountService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  //회원가입
  @PostMapping
  public ResponseEntity<Map<String, Boolean>> joinAccount(@RequestBody RequestAddUser user) {
    return ResponseEntity.ok().body(
        Map.of("result", accountService.addAccount(new ModelMapper().map(user, UserDTO.class))));
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
  public ResponseEntity<Map<String, String>> setImage(@RequestParam("file") MultipartFile file,
      Authentication auth){
    if (file.isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("message", "파일이 비어 있습니다."));
    }

    AccountDetails accountDetails = (AccountDetails) auth.getPrincipal();
    UserDTO userDTO = new ModelMapper().map(accountDetails.getUser(), UserDTO.class);

    String originalFilename = file.getOriginalFilename();
    String safeFilename = originalFilename.replaceAll("\\s+", "_");
    String newFilename = UUID.randomUUID() + "_" + safeFilename;

    userDTO.setImage(newFilename);
    accountService.setUserImage(userDTO);

    return ResponseEntity.ok(Map.of("message", "업로드 성공"));
  }
}

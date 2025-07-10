package com.oopsw.accountservice.auth;

import com.oopsw.accountservice.account.jpa.AccountRepository;
import com.oopsw.accountservice.account.jpa.UserEntity;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountOauth2UserService extends DefaultOAuth2UserService {
  private final AccountRepository accountRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String email = null;
    String nickname = null;
    String fakePassword = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());
    if ("google".equals(registrationId)) {
      email = oAuth2User.getAttribute("email");
      nickname = oAuth2User.getAttribute("name");
    } else if ("kakao".equals(registrationId)) {
      Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
      Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

      email = (String) kakaoAccount.get("email");
      nickname = (String) profile.get("nickname");
    }

    if (email == null) throw new OAuth2AuthenticationException("이메일 정보를 가져올 수 없습니다.");

    if (accountRepository.existsByNickname(nickname)) {
      nickname = nickname + UUID.randomUUID().toString().substring(0, 5); // 임시 닉네임
    }

    UserEntity user = accountRepository.findByEmail(email);

    if(user == null) {
      user.setEmail(email);
      user.setNickname(nickname);
      user.setEncryptedPwd(fakePassword);
      user.setBirthDate(LocalDate.of(1999,2,20));
      user.setPhoneNumber("010-2323-2323");
      user.setOauth(true);
      accountRepository.save(user);
    }
    return new AccountDetails(user, oAuth2User.getAttributes());
  }

}

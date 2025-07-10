package com.oopsw.accountservice.account.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString(exclude = "encryptedPwd")
@Table(name = "users")
public class UserEntity {

  @Id
  @Column(length = 320)
  String email;

  @Column(nullable = false, unique = true, length = 200)
  String nickname;
  @Column(name = "ENCRYPTED_PASSWORD", nullable = false, length = 100)
  String encryptedPwd;
  @Column(name = "BIRTH_DATE", nullable = false)
  LocalDate birthDate;
  @Column(name = "PHONE_NUMBER", nullable = false, length = 20)
  String phoneNumber;
  @Column(name = "JOIN_DATE", nullable = false)
  LocalDate joinDate;
  @Column(length = 500)
  String image;
  @Column(nullable = false, length = 20)
  String role;

  @Column(name = "IS_OAUTH", nullable = false)
  boolean isOauth;

  @PrePersist
  public void prePersist() {
    if (this.joinDate == null) {
      this.joinDate = LocalDate.now();
    }
  }
}

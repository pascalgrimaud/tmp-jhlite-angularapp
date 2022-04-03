package tech.jhipster.angularapp.account.infrastructure.primary.rest;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.jhipster.angularapp.UnitTest;

@UnitTest
class LoginDTOTest {

  @Test
  void shouldBuild() {
    LoginDTO loginDTO = new LoginDTO();
    loginDTO.setUsername("admin");
    loginDTO.setPassword("password");
    loginDTO.setRememberMe(true);

    assertThat(loginDTO.getUsername()).isEqualTo("admin");
    assertThat(loginDTO.getPassword()).isEqualTo("password");
    assertThat(loginDTO.isRememberMe()).isTrue();

    assertThat(loginDTO.toString()).contains("admin");
    assertThat(loginDTO.toString()).doesNotContain("password");
  }
}

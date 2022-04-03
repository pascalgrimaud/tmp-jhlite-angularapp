package tech.jhipster.angularapp.account.infrastructure.primary.rest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tech.jhipster.angularapp.IntegrationTest;
import tech.jhipster.angularapp.TestUtil;

@IntegrationTest
@AutoConfigureMockMvc
class AuthenticationResourceIT {

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  MockMvc mockMvc;

  @Test
  void shouldAuthorize() throws Exception {
    LoginDTO login = new LoginDTO();
    login.setUsername("admin");
    login.setPassword("admin");
    mockMvc
      .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id_token").isString())
      .andExpect(jsonPath("$.id_token").isNotEmpty())
      .andExpect(header().string("Authorization", not(nullValue())))
      .andExpect(header().string("Authorization", not(is(emptyString()))));
  }

  @Test
  void shouldAuthorizeWithRememberMe() throws Exception {
    LoginDTO login = new LoginDTO();
    login.setUsername("admin");
    login.setPassword("admin");
    login.setRememberMe(true);
    mockMvc
      .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id_token").isString())
      .andExpect(jsonPath("$.id_token").isNotEmpty())
      .andExpect(header().string("Authorization", not(nullValue())))
      .andExpect(header().string("Authorization", not(is(emptyString()))));
  }

  @Test
  void shouldNotAuthorize() throws Exception {
    LoginDTO login = new LoginDTO();
    login.setUsername("wrong-user");
    login.setPassword("wrong password");
    mockMvc
      .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login)))
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.id_token").doesNotExist())
      .andExpect(header().doesNotExist("Authorization"));
  }

  @Test
  @WithMockUser
  void shouldBeAuthenticated() throws Exception {
    mockMvc.perform(get("/api/authenticate")).andExpect(status().isOk()).andExpect(content().string(containsString("user")));
  }

  @Test
  void shouldBuildJWTToken() {
    AuthenticationResource.JWTToken token = new AuthenticationResource.JWTToken("chips");
    token.setIdToken("beer");

    assertThat(token.getIdToken()).isEqualTo("beer");
  }
}

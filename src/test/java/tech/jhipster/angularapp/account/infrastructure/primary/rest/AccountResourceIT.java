package tech.jhipster.angularapp.account.infrastructure.primary.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tech.jhipster.angularapp.IntegrationTest;
import tech.jhipster.angularapp.security.jwt.domain.AuthoritiesConstants;

@IntegrationTest
@AutoConfigureMockMvc
class AccountResourceIT {

  @Autowired
  MockMvc mockMvc;

  @Test
  @WithMockUser
  void shouldAccount() throws Exception {
    mockMvc
      .perform(get("/api/account"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.login").value("user"))
      .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.USER));
  }

  @Test
  @WithAnonymousUser
  void shouldNotAccount() throws Exception {
    mockMvc.perform(get("/api/account")).andExpect(status().isUnauthorized());
  }
}

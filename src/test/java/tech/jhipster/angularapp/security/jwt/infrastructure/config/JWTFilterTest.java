package tech.jhipster.angularapp.security.jwt.infrastructure.config;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import tech.jhipster.angularapp.UnitTest;
import tech.jhipster.angularapp.security.jwt.domain.AuthoritiesConstants;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@UnitTest
class JWTFilterTest {

  private TokenProvider tokenProvider;

  private JWTFilter jwtFilter;

  @BeforeEach
  public void setup() {
    ApplicationSecurityProperties properties = new ApplicationSecurityProperties();
    String base64Secret = "fd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8";
    properties.getAuthentication().getJwt().setBase64Secret(base64Secret);
    tokenProvider = new TokenProvider(properties);
    ReflectionTestUtils.setField(tokenProvider, "key", Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret)));

    ReflectionTestUtils.setField(tokenProvider, "tokenValidityInMilliseconds", 60000);
    jwtFilter = new JWTFilter(tokenProvider);
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  @Test
  void testJWTFilter() throws Exception {
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
      "test-user",
      "test-password",
      Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
    );
    String jwt = tokenProvider.createToken(authentication, false);
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
    request.setRequestURI("/api/test");
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    jwtFilter.doFilter(request, response, filterChain);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("test-user");
    assertThat(SecurityContextHolder.getContext().getAuthentication().getCredentials()).hasToString(jwt);
  }

  @Test
  void testJWTFilterInvalidToken() throws Exception {
    String jwt = "wrong_jwt";
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
    request.setRequestURI("/api/test");
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    jwtFilter.doFilter(request, response, filterChain);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  void testJWTFilterMissingAuthorization() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/test");
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    jwtFilter.doFilter(request, response, filterChain);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  void testJWTFilterMissingToken() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Bearer ");
    request.setRequestURI("/api/test");
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    jwtFilter.doFilter(request, response, filterChain);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  void testJWTFilterWrongScheme() throws Exception {
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
      "test-user",
      "test-password",
      Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
    );
    String jwt = tokenProvider.createToken(authentication, false);
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Basic " + jwt);
    request.setRequestURI("/api/test");
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    jwtFilter.doFilter(request, response, filterChain);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }
}

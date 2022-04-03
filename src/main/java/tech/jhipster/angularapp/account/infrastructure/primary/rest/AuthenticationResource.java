package tech.jhipster.angularapp.account.infrastructure.primary.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.jhipster.angularapp.security.jwt.infrastructure.config.JWTFilter;
import tech.jhipster.angularapp.security.jwt.infrastructure.config.TokenProvider;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authenticate")
class AuthenticationResource {

  private final Logger log = LoggerFactory.getLogger(AuthenticationResource.class);

  private final TokenProvider tokenProvider;

  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  public AuthenticationResource(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
    this.tokenProvider = tokenProvider;
    this.authenticationManagerBuilder = authenticationManagerBuilder;
  }

  @GetMapping
  public String isAuthenticated(HttpServletRequest request) {
    log.debug("REST request to check if the current user is authenticated");
    return request.getRemoteUser();
  }

  @PostMapping
  public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginDTO loginDTO) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
      loginDTO.getUsername(),
      loginDTO.getPassword()
    );

    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.createToken(authentication, loginDTO.isRememberMe());
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
    return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
  }

  /**
   * Object to return as body in JWT Authentication.
   */
  static class JWTToken {

    private String idToken;

    JWTToken(String idToken) {
      this.idToken = idToken;
    }

    @JsonProperty("id_token")
    String getIdToken() {
      return idToken;
    }

    void setIdToken(String idToken) {
      this.idToken = idToken;
    }
  }
}

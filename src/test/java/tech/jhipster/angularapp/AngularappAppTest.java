package tech.jhipster.angularapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;
import static tech.jhipster.angularapp.AngularappApp.LF;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@UnitTest
@ExtendWith(SpringExtension.class)
class AngularappAppTest {

  @Mock
  ConfigurableApplicationContext applicationContext;

  @Mock
  ConfigurableEnvironment environment;

  @Test
  void shouldConstruct() {
    assertThatCode(() -> new AngularappApp()).doesNotThrowAnyException();
  }

  @Test
  void shouldMain() {
    try (MockedStatic<SpringApplication> springApplication = Mockito.mockStatic(SpringApplication.class)) {
      when(applicationContext.getEnvironment()).thenReturn(environment);
      springApplication.when(() -> SpringApplication.run(AngularappApp.class, new String[] {})).thenReturn(applicationContext);

      assertThatCode(() -> AngularappApp.main(new String[] {})).doesNotThrowAnyException();
    }
  }

  @Test
  void shouldApplicationRunning() {
    String result = AngularappApp.applicationRunning("jhlite");
    assertThat(result).isEqualTo("  Application 'jhlite' is running!" + LF);
  }

  @Test
  void shouldAccessUrlLocalWithoutServerPort() {
    String result = AngularappApp.accessUrlLocal(null, null, null);
    assertThat(result).isEmpty();
  }

  @Test
  void shouldAccessUrlLocalWithoutContextPath() {
    String result = AngularappApp.accessUrlLocal("http", "8080", "/");
    assertThat(result).isEqualTo("  Local: \thttp://localhost:8080/" + LF);
  }

  @Test
  void shouldAccessUrlLocalWithContextPath() {
    String result = AngularappApp.accessUrlLocal("http", "8080", "/lite/");
    assertThat(result).isEqualTo("  Local: \thttp://localhost:8080/lite/" + LF);
  }

  @Test
  void shouldAccessUrlExternalWithoutServerPort() {
    String result = AngularappApp.accessUrlExternal(null, null, null, null);
    assertThat(result).isEmpty();
  }

  @Test
  void shouldAccessUrlExternalWithoutContextPath() {
    String result = AngularappApp.accessUrlExternal("http", "127.0.1.1", "8080", "/");
    assertThat(result).isEqualTo("  External: \thttp://127.0.1.1:8080/" + LF);
  }

  @Test
  void shouldAccessUrlExternalWithContextPath() {
    String result = AngularappApp.accessUrlExternal("http", "127.0.1.1", "8080", "/lite/");
    assertThat(result).isEqualTo("  External: \thttp://127.0.1.1:8080/lite/" + LF);
  }

  @Test
  void shouldConfigServerWithoutConfigServerStatus() {
    String result = AngularappApp.configServer(null);
    assertThat(result).isEmpty();
  }

  @Test
  void shouldConfigServer() {
    String result = AngularappApp.configServer("Connected to the JHipster Registry running in Docker");
    assertThat(result).contains("Config Server: Connected to the JHipster Registry running in Docker");
  }

  @Test
  void shouldGetProtocol() {
    assertThat(AngularappApp.getProtocol(null)).isEqualTo("http");
  }

  @Test
  void shouldGetProtocolForBlank() {
    assertThat(AngularappApp.getProtocol(" ")).isEqualTo("https");
  }

  @Test
  void shouldGetProtocolForValue() {
    assertThat(AngularappApp.getProtocol("https")).isEqualTo("https");
  }

  @Test
  void shouldGetContextPath() {
    assertThat(AngularappApp.getContextPath("/chips")).isEqualTo("/chips");
  }

  @Test
  void shouldGetContextPathForNull() {
    assertThat(AngularappApp.getContextPath(null)).isEqualTo("/");
  }

  @Test
  void shouldGetContextPathForBlank() {
    assertThat(AngularappApp.getContextPath(" ")).isEqualTo("/");
  }

  @Test
  void shouldGetHost() {
    assertThatCode(AngularappApp::getHostAddress).doesNotThrowAnyException();
  }

  @Test
  void shouldGetHostWithoutHostAddress() {
    try (MockedStatic<InetAddress> inetAddress = Mockito.mockStatic(InetAddress.class)) {
      inetAddress.when(InetAddress::getLocalHost).thenThrow(new UnknownHostException());

      String result = AngularappApp.getHostAddress();

      assertThat(result).isEqualTo("localhost");
    }
  }
}

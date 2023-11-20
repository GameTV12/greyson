package eu.greyson.bank.general.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.greyson.bank.general.config.JwtService;
import eu.greyson.bank.general.model.User;
import eu.greyson.bank.general.repository.UserRepository;
import eu.greyson.bank.shared.dto.auth.AuthResponse;
import eu.greyson.bank.shared.dto.auth.LoginRequest;
import eu.greyson.bank.shared.model.BaseEntity;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.webjars.NotFoundException;

@ContextConfiguration(classes = {AuthService.class})
@ExtendWith(SpringExtension.class)
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link AuthService#authenticate(LoginRequest)}
     */
    @Test
    void testAuthenticate() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        LoginRequest dto = new LoginRequest();
        dto.setEmail("jane.doe@example.org");
        dto.setPassword("iloveyou");
        assertThrows(NotFoundException.class, () -> authService.authenticate(dto));
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link AuthService#authenticate(LoginRequest)}
     */
    @Test
    void testAuthenticate2() throws EntityNotFoundException, AuthenticationException {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);

        User user2 = new User();
        user2.setAccounts(new ArrayList<>());
        user2.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(BaseEntity.serialVersionUID);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        when(userRepository.findUnsafe(Mockito.<Long>any())).thenReturn(user2);
        when(userRepository.findAll()).thenReturn(userList);
        when(jwtService.generateAccessToken(Mockito.<User>any())).thenReturn("ABC123");
        when(jwtService.generateRefreshToken(Mockito.<User>any())).thenReturn("ABC123");
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(true);
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        LoginRequest dto = new LoginRequest();
        dto.setEmail("jane.doe@example.org");
        dto.setPassword("iloveyou");
        AuthResponse actualAuthenticateResult = authService.authenticate(dto);
        verify(jwtService).generateAccessToken(Mockito.<User>any());
        verify(jwtService).generateRefreshToken(Mockito.<User>any());
        verify(userRepository).findUnsafe(Mockito.<Long>any());
        verify(userRepository).findAll();
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
        verify(passwordEncoder).matches(Mockito.<CharSequence>any(), Mockito.<String>any());
        assertEquals("ABC123", actualAuthenticateResult.getAccessToken());
        assertEquals("ABC123", actualAuthenticateResult.getRefreshToken());
    }

    /**
     * Method under test: {@link AuthService#authenticate(LoginRequest)}
     */
    @Test
    void testAuthenticate3() throws AuthenticationException {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(true);
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenThrow(new NotFoundException("An error occurred"));

        LoginRequest dto = new LoginRequest();
        dto.setEmail("jane.doe@example.org");
        dto.setPassword("iloveyou");
        assertThrows(NotFoundException.class, () -> authService.authenticate(dto));
        verify(userRepository).findAll();
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
        verify(passwordEncoder).matches(Mockito.<CharSequence>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#authenticate(LoginRequest)}
     */
    @Test
    void testAuthenticate4() throws EntityNotFoundException, AuthenticationException {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");

        User user2 = new User();
        user2.setAccounts(new ArrayList<>());
        user2.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user2.setEmail("john.smith@example.org");
        user2.setFirstName("John");
        user2.setId(2L);
        user2.setLastName("Smith");
        user2.setPassword("Password");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user2);
        userList.add(user);

        User user3 = new User();
        user3.setAccounts(new ArrayList<>());
        user3.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(BaseEntity.serialVersionUID);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        when(userRepository.findUnsafe(Mockito.<Long>any())).thenReturn(user3);
        when(userRepository.findAll()).thenReturn(userList);
        when(jwtService.generateAccessToken(Mockito.<User>any())).thenReturn("ABC123");
        when(jwtService.generateRefreshToken(Mockito.<User>any())).thenReturn("ABC123");
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(true);
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        LoginRequest dto = new LoginRequest();
        dto.setEmail("jane.doe@example.org");
        dto.setPassword("iloveyou");
        AuthResponse actualAuthenticateResult = authService.authenticate(dto);
        verify(jwtService).generateAccessToken(Mockito.<User>any());
        verify(jwtService).generateRefreshToken(Mockito.<User>any());
        verify(userRepository).findUnsafe(Mockito.<Long>any());
        verify(userRepository).findAll();
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
        verify(passwordEncoder).matches(Mockito.<CharSequence>any(), Mockito.<String>any());
        assertEquals("ABC123", actualAuthenticateResult.getAccessToken());
        assertEquals("ABC123", actualAuthenticateResult.getRefreshToken());
    }

    /**
     * Method under test: {@link AuthService#authenticate(LoginRequest)}
     */
    @Test
    void testAuthenticate5() {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(false);

        LoginRequest dto = new LoginRequest();
        dto.setEmail("jane.doe@example.org");
        dto.setPassword("iloveyou");
        assertThrows(NotFoundException.class, () -> authService.authenticate(dto));
        verify(userRepository).findAll();
        verify(passwordEncoder).matches(Mockito.<CharSequence>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#refreshToken(HttpServletRequest)}
     */
    @Test
    void testRefreshToken() throws IOException {
        assertThrows(IOException.class, () -> authService.refreshToken(new MockHttpServletRequest()));
    }

    /**
     * Method under test: {@link AuthService#refreshToken(HttpServletRequest)}
     */
    @Test
    void testRefreshToken2() throws IOException {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("https://example.org/example");
        assertThrows(IOException.class, () -> authService.refreshToken(request));
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#refreshToken(HttpServletRequest)}
     */
    @Test
    void testRefreshToken3() throws IOException, EntityNotFoundException {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        when(userService.getUnsafe(Mockito.<Long>any())).thenReturn(user);
        when(jwtService.generateAccessToken(Mockito.<User>any())).thenReturn("ABC123");
        when(jwtService.generateRefreshToken(Mockito.<User>any())).thenReturn("ABC123");
        when(jwtService.isTokenValid(Mockito.<String>any(), Mockito.<User>any())).thenReturn(true);
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn("42");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        AuthResponse actualRefreshTokenResult = authService.refreshToken(request);
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(jwtService).generateAccessToken(Mockito.<User>any());
        verify(jwtService).generateRefreshToken(Mockito.<User>any());
        verify(jwtService).isTokenValid(Mockito.<String>any(), Mockito.<User>any());
        verify(userService).getUnsafe(Mockito.<Long>any());
        verify(request).getHeader(Mockito.<String>any());
        assertEquals("ABC123", actualRefreshTokenResult.getAccessToken());
        assertEquals("ABC123", actualRefreshTokenResult.getRefreshToken());
    }

    /**
     * Method under test: {@link AuthService#refreshToken(HttpServletRequest)}
     */
    @Test
    void testRefreshToken4() throws IOException, EntityNotFoundException {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        when(userService.getUnsafe(Mockito.<Long>any())).thenReturn(user);
        when(jwtService.generateAccessToken(Mockito.<User>any())).thenThrow(new IOException("Authorization"));
        when(jwtService.isTokenValid(Mockito.<String>any(), Mockito.<User>any())).thenReturn(true);
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn("42");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        assertThrows(IOException.class, () -> authService.refreshToken(request));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(jwtService).generateAccessToken(Mockito.<User>any());
        verify(jwtService).isTokenValid(Mockito.<String>any(), Mockito.<User>any());
        verify(userService).getUnsafe(Mockito.<Long>any());
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#refreshToken(HttpServletRequest)}
     */
    @Test
    void testRefreshToken5() throws IOException, EntityNotFoundException {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        when(userService.getUnsafe(Mockito.<Long>any())).thenReturn(user);
        when(jwtService.isTokenValid(Mockito.<String>any(), Mockito.<User>any())).thenReturn(false);
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn("42");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        assertThrows(IOException.class, () -> authService.refreshToken(request));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(jwtService).isTokenValid(Mockito.<String>any(), Mockito.<User>any());
        verify(userService).getUnsafe(Mockito.<Long>any());
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#refreshToken(HttpServletRequest)}
     */
    @Test
    void testRefreshToken6() throws IOException {
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn(null);
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        assertThrows(IOException.class, () -> authService.refreshToken(request));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(request).getHeader(Mockito.<String>any());
    }
}

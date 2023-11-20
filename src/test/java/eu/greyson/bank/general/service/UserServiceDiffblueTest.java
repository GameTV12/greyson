package eu.greyson.bank.general.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import eu.greyson.bank.general.config.JwtService;
import eu.greyson.bank.general.dto.GetUserDto;
import eu.greyson.bank.general.model.User;
import eu.greyson.bank.general.repository.UserRepository;
import eu.greyson.bank.shared.dto.auth.RegisterRequest;
import eu.greyson.bank.shared.exception.BadRequestException;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @MockBean
    private JwtService jwtService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Method under test: {@link UserService#createDto(User)}
     */
    @Test
    void testCreateDto() {
        GetUserDto getUserDto = new GetUserDto();
        getUserDto.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getUserDto.setFirstName("Jane");
        getUserDto.setId(1L);
        getUserDto.setLastName("Doe");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetUserDto>>any())).thenReturn(getUserDto);

        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        GetUserDto actualCreateDtoResult = userService.createDto(user);
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetUserDto>>any());
        assertSame(getUserDto, actualCreateDtoResult);
    }

    /**
     * Method under test: {@link UserService#createDto(User)}
     */
    @Test
    void testCreateDto2() {
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetUserDto>>any())).thenThrow(new IOException("Msg"));

        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        assertThrows(IOException.class, () -> userService.createDto(user));
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetUserDto>>any());
    }

    /**
     * Method under test: {@link UserService#getUser(HttpServletRequest)}
     */
    @Test
    void testGetUser() {
        assertThrows(IOException.class, () -> userService.getUser(new MockHttpServletRequest()));
    }

    /**
     * Method under test: {@link UserService#getUser(HttpServletRequest)}
     */
    @Test
    void testGetUser2() {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("https://example.org/example");
        assertThrows(IOException.class, () -> userService.getUser(request));
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#getUser(HttpServletRequest)}
     */
    @Test
    void testGetUser3() throws EntityNotFoundException {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        when(userRepository.findUnsafe(Mockito.<Long>any())).thenReturn(user);

        GetUserDto getUserDto = new GetUserDto();
        getUserDto.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getUserDto.setFirstName("Jane");
        getUserDto.setId(1L);
        getUserDto.setLastName("Doe");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetUserDto>>any())).thenReturn(getUserDto);
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn("42");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        GetUserDto actualUser = userService.getUser(request);
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(userRepository).findUnsafe(Mockito.<Long>any());
        verify(request).getHeader(Mockito.<String>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetUserDto>>any());
        assertSame(getUserDto, actualUser);
    }

    /**
     * Method under test: {@link UserService#getUser(HttpServletRequest)}
     */
    @Test
    void testGetUser4() throws EntityNotFoundException {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        when(userRepository.findUnsafe(Mockito.<Long>any())).thenReturn(user);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetUserDto>>any()))
                .thenThrow(new IOException("Authorization"));
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn("42");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        assertThrows(IOException.class, () -> userService.getUser(request));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(userRepository).findUnsafe(Mockito.<Long>any());
        verify(request).getHeader(Mockito.<String>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetUserDto>>any());
    }

    /**
     * Method under test: {@link UserService#createUser(RegisterRequest)}
     */
    @Test
    void testCreateUser() {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);

        GetUserDto getUserDto = new GetUserDto();
        getUserDto.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getUserDto.setFirstName("Jane");
        getUserDto.setId(1L);
        getUserDto.setLastName("Doe");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetUserDto>>any())).thenReturn(getUserDto);
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        RegisterRequest dto = new RegisterRequest();
        dto.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        dto.setEmail("jane.doe@example.org");
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setPassword("iloveyou");
        GetUserDto actualCreateUserResult = userService.createUser(dto);
        verify(userRepository).save(Mockito.<User>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetUserDto>>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
        assertSame(getUserDto, actualCreateUserResult);
    }

    /**
     * Method under test: {@link UserService#updateUser(HttpServletRequest, RegisterRequest, Long)}
     */
    @Test
    void testUpdateUser() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        RegisterRequest dto = new RegisterRequest();
        dto.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        dto.setEmail("jane.doe@example.org");
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setPassword("iloveyou");
        assertThrows(IOException.class, () -> userService.updateUser(request, dto, 1L));
    }

    /**
     * Method under test: {@link UserService#updateUser(HttpServletRequest, RegisterRequest, Long)}
     */
    @Test
    void testUpdateUser2() {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("https://example.org/example");

        RegisterRequest dto = new RegisterRequest();
        dto.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        dto.setEmail("jane.doe@example.org");
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setPassword("iloveyou");
        assertThrows(IOException.class, () -> userService.updateUser(request, dto, 1L));
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#updateUser(HttpServletRequest, RegisterRequest, Long)}
     */
    @Test
    void testUpdateUser3() {
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn("42");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");

        RegisterRequest dto = new RegisterRequest();
        dto.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        dto.setEmail("jane.doe@example.org");
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setPassword("iloveyou");
        assertThrows(BadRequestException.class, () -> userService.updateUser(request, dto, 1L));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#updateUser(HttpServletRequest, RegisterRequest, Long)}
     */
    @Test
    void testUpdateUser4() {
        when(jwtService.extractUserId(Mockito.<String>any())).thenThrow(new IOException("Authorization"));
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");

        RegisterRequest dto = new RegisterRequest();
        dto.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        dto.setEmail("jane.doe@example.org");
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setPassword("iloveyou");
        assertThrows(IOException.class, () -> userService.updateUser(request, dto, 1L));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(request).getHeader(Mockito.<String>any());
    }
}

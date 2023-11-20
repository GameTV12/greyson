package eu.greyson.bank.general.rest.auth;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.greyson.bank.general.service.AuthService;
import eu.greyson.bank.general.service.UserService;
import eu.greyson.bank.shared.dto.auth.AuthResponse;
import eu.greyson.bank.shared.dto.auth.LoginRequest;
import eu.greyson.bank.shared.dto.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthControllerImpl.class})
@ExtendWith(SpringExtension.class)
class AuthControllerImplTest {
    @Autowired
    private AuthControllerImpl authControllerImpl;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link AuthControllerImpl#authenticate(LoginRequest)}
     */
    @Test
    void testAuthenticate() throws Exception {
        AuthResponse buildResult = AuthResponse.builder().accessToken("ABC123").refreshToken("ABC123").build();
        when(authService.authenticate(Mockito.<LoginRequest>any())).thenReturn(buildResult);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("jane.doe@example.org");
        loginRequest.setPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(authControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"accessToken\":\"ABC123\",\"refreshToken\":\"ABC123\"}"));
    }

    /**
     * Method under test: {@link AuthControllerImpl#refreshToken(HttpServletRequest)}
     */
    @Test
    void testRefreshToken() throws Exception {
        AuthResponse buildResult = AuthResponse.builder().accessToken("ABC123").refreshToken("ABC123").build();
        when(authService.refreshToken(Mockito.<HttpServletRequest>any())).thenReturn(buildResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/auth/refresh-token");
        MockMvcBuilders.standaloneSetup(authControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"accessToken\":\"ABC123\",\"refreshToken\":\"ABC123\"}"));
    }

    /**
     * Method under test: {@link AuthControllerImpl#createUser(RegisterRequest)}
     */
    @Test
    void testCreateUser() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        registerRequest.setEmail("jane.doe@example.org");
        registerRequest.setFirstName("Jane");
        registerRequest.setLastName("Doe");
        registerRequest.setPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(registerRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authControllerImpl)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

    /**
     * Method under test: {@link AuthControllerImpl#createUser(RegisterRequest)}
     */
    @Test
    void testCreateUser2() throws Exception {
        java.sql.Date birthDate = mock(java.sql.Date.class);
        when(birthDate.getTime()).thenReturn(10L);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setBirthDate(birthDate);
        registerRequest.setEmail("jane.doe@example.org");
        registerRequest.setFirstName("Jane");
        registerRequest.setLastName("Doe");
        registerRequest.setPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(registerRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authControllerImpl)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }
}

package eu.greyson.bank.general.rest.user;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.greyson.bank.general.dto.GetUserDto;
import eu.greyson.bank.general.service.UserService;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserControllerImpl.class})
@ExtendWith(SpringExtension.class)
class UserControllerImplTest {
    @Autowired
    private UserControllerImpl userControllerImpl;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link UserControllerImpl#getUser(HttpServletRequest)}
     */
    @Test
    void testGetUser() throws Exception {
        GetUserDto getUserDto = new GetUserDto();
        getUserDto.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getUserDto.setFirstName("Jane");
        getUserDto.setId(1L);
        getUserDto.setLastName("Doe");
        when(userService.getUser(Mockito.<HttpServletRequest>any())).thenReturn(getUserDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user");
        MockMvcBuilders.standaloneSetup(userControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"birthDate\":0}"));
    }

    /**
     * Method under test: {@link UserControllerImpl#updateUser(HttpServletRequest, RegisterRequest, Long)}
     */
    @Test
    void testUpdateUser() throws Exception {
        GetUserDto getUserDto = new GetUserDto();
        getUserDto.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getUserDto.setFirstName("Jane");
        getUserDto.setId(1L);
        getUserDto.setLastName("Doe");
        when(userService.updateUser(Mockito.<HttpServletRequest>any(), Mockito.<RegisterRequest>any(), Mockito.<Long>any()))
                .thenReturn(getUserDto);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        registerRequest.setEmail("jane.doe@example.org");
        registerRequest.setFirstName("Jane");
        registerRequest.setLastName("Doe");
        registerRequest.setPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(registerRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/user/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"birthDate\":0}"));
    }
}

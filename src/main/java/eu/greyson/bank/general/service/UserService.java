package eu.greyson.bank.general.service;

import eu.greyson.bank.general.config.JwtService;
import eu.greyson.bank.general.dto.GetUserDto;
import eu.greyson.bank.general.model.User;
import eu.greyson.bank.general.repository.UserRepository;
import eu.greyson.bank.shared.dto.auth.RegisterRequest;
import eu.greyson.bank.shared.exception.BadRequestException;
import eu.greyson.bank.shared.service.BaseService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService<User, Long, UserRepository> implements InitializingBean {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected UserRepository getDefaultRepository() {
        return userRepository;
    }

    @Override
    public void afterPropertiesSet() {
        mapper.addMappings(new UserDtoMapper());
    }

    private static class UserDtoMapper extends PropertyMap<User, GetUserDto> {
        @Override
        protected void configure() {
            map().setBirthDate(source.getBirthDate());
            map().setFirstName(source.getFirstName());
            map().setLastName(source.getLastName());
        }
    }

    public GetUserDto createDto(User user) {
        return mapper.map(user, GetUserDto.class);
    }

    public GetUserDto getUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new IOException("Not user");
        String token = authHeader.substring(7);
        final String userId = jwtService.extractUserId(token);
        if (userId != null) {
            return createDto(getUnsafe(Long.parseLong(userId)));
        }
        throw new IOException("Not user");
    }

    public GetUserDto createUser(RegisterRequest dto){
        User newUser = new User();
        newUser.setBirthDate(dto.getBirthDate());
        newUser.setLastName(dto.getLastName());
        newUser.setFirstName(dto.getFirstName());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setEmail(dto.getEmail());
        userRepository.save(newUser);
        return createDto(newUser);
    }

    public GetUserDto updateUser(HttpServletRequest request, RegisterRequest dto, Long id){
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new IOException("Not user");
        String token = authHeader.substring(7);
        if (!Objects.equals(jwtService.extractUserId(token), id+"")) throw new BadRequestException("User isn't allowed");

        User newUser = getUnsafe(id);
        newUser.setBirthDate(dto.getBirthDate());
        newUser.setLastName(dto.getLastName());
        newUser.setFirstName(dto.getFirstName());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setEmail(dto.getEmail());
        userRepository.save(newUser);
        return createDto(newUser);
    }
}

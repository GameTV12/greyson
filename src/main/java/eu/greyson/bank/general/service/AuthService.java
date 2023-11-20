package eu.greyson.bank.general.service;

import eu.greyson.bank.general.config.JwtService;
import eu.greyson.bank.general.model.User;
import eu.greyson.bank.general.repository.UserRepository;
import eu.greyson.bank.shared.dto.auth.AuthResponse;
import eu.greyson.bank.shared.dto.auth.LoginRequest;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse authenticate(LoginRequest dto) {
        Optional<User> userByEmail = userRepository.findAll().stream().filter(x -> Objects.equals(x.getEmail(), dto.getEmail())).findFirst();
        if (userByEmail.isEmpty() || !passwordEncoder.matches(dto.getPassword(), userByEmail.get().getPassword())) {
            throw new NotFoundException("User not found");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userByEmail.get().getId()+"",
                        dto.getPassword()
                )
        );
        var user = userRepository.findUnsafe(userByEmail.get().getId());
        String jwtAccessToken = jwtService.generateAccessToken(user);
        String jwtRefreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    public AuthResponse refreshToken(
            HttpServletRequest request
    ) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IOException("Not user");
        }
        String token = authHeader.substring(7);
        final String userId = jwtService.extractUserId(token);
        if (userId != null) {
            User user = this.userService.getUnsafe(Long.parseLong(userId));
            if (jwtService.isTokenValid(token, user)) {
                String accessToken = jwtService.generateAccessToken(user);
                token = jwtService.generateRefreshToken(user);
                return AuthResponse.builder()
                        .refreshToken(token)
                        .accessToken(accessToken)
                        .build();
            }
        }
        throw new IOException("Not user");
    }
}

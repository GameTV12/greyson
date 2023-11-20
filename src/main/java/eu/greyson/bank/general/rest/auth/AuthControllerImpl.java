package eu.greyson.bank.general.rest.auth;

import com.fasterxml.jackson.annotation.JsonView;
import eu.greyson.bank.general.dto.GetUserDto;
import eu.greyson.bank.general.service.AuthService;
import eu.greyson.bank.general.service.UserService;
import eu.greyson.bank.shared.dto.auth.AuthResponse;
import eu.greyson.bank.shared.dto.auth.LoginRequest;
import eu.greyson.bank.shared.dto.auth.RegisterRequest;
import eu.greyson.bank.shared.json.DtoView;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController{
    private final AuthService authService;
    private final UserService userService;

    @Override
    @PostMapping
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<GetUserDto> createUser(@RequestBody() RegisterRequest dto) {
        var user = userService.createUser(dto);
        return ResponseEntity.ok(user);
    }

    @Override
    @PostMapping("/login")
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<AuthResponse> authenticate(@RequestBody LoginRequest dto) {
        return ResponseEntity.ok(authService.authenticate(dto));
    }

    @Override
    @PutMapping ("/refresh-token")
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}

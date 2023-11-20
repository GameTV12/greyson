package eu.greyson.bank.general.rest.user;

import com.fasterxml.jackson.annotation.JsonView;
import eu.greyson.bank.general.dto.GetUserDto;
import eu.greyson.bank.general.service.UserService;
import eu.greyson.bank.shared.dto.auth.RegisterRequest;
import eu.greyson.bank.shared.json.DtoView;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController{
    private final UserService userService;

    @Override
    @GetMapping
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<GetUserDto> getUser(HttpServletRequest request) {
        var user = userService.getUser(request);
        return ResponseEntity.ok(user);
    }

    @Override
    @PutMapping("{userId}")
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<GetUserDto> updateUser(HttpServletRequest request, @RequestBody RegisterRequest dto, @PathVariable(name = "userId") Long id) {
        var user = userService.updateUser(request, dto, id);
        return ResponseEntity.ok(user);
    }
}


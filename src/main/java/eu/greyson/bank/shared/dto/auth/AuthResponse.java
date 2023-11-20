package eu.greyson.bank.shared.dto.auth;

import com.fasterxml.jackson.annotation.JsonView;
import eu.greyson.bank.shared.json.DtoView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonView(DtoView.Detail.class)
public class AuthResponse {
    @Schema(description = "JWT access token")
    private String accessToken;

    @Schema(description = "JWT refresh token")
    private String refreshToken;
}
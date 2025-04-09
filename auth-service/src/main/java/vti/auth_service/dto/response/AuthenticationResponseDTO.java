package vti.auth_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthenticationResponseDTO {
    private int status;
    private String message;

    @JsonProperty("user_id")
    private int userId;

//    @JsonProperty("access_token")
    private String accessToken;

//    @JsonProperty("refresh_token")
    private String refreshToken;
}

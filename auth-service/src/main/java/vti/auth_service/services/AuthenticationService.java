package vti.auth_service.services;

import io.jsonwebtoken.Jwts;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vti.auth_service.dto.request.LoginRequestDTO;
import vti.auth_service.dto.request.RegisterRequestDTO;
import vti.auth_service.dto.response.AuthenticationResponseDTO;
import vti.auth_service.dto.response.RegisterResponseDTO;
import vti.auth_service.exception.CustomException;
import vti.auth_service.model.Role;
import vti.auth_service.model.User;
import vti.auth_service.user.repo.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final int TOKEN_INDEX = 7;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        //check email, username is existed in db
        String email = registerRequestDTO.getEmail();
        String username = registerRequestDTO.getUsername();

        Optional<User> userFoundByEmail = userRepository.findByEmail(email);
        Optional<User> userFoundByUsername = userRepository.findByUsername(username);

        if(userFoundByEmail.isPresent() && userFoundByUsername.isPresent()) {
            throw new ConstraintViolationException("User already exitsts!",null);
        }

        // Create user and s ave user into db
        User user = User.builder()
                .username(registerRequestDTO.getUsername())
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(Role.toEnum(registerRequestDTO.getRole()))
                .build();

        userRepository.save(user);

        return RegisterResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("User created")
                .build();
    }

    public AuthenticationResponseDTO login(LoginRequestDTO loginRequestDTO){
        //authenticate username and password
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        //generate access token and refresh token
        Optional<User> userFoundByUsername = userRepository.findByUsername(username);
        if(userFoundByUsername.isPresent()) {
            User user = userFoundByUsername.get();
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);


            //save access token and refresh token into user in database
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            //response access token and refresh token to client
            return AuthenticationResponseDTO.builder()
                    .status(HttpStatus.OK.value())
                    .message("Login successfully")
                    .userId(user.getId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }else {
            return AuthenticationResponseDTO.builder()
                    .status(HttpStatus.FORBIDDEN.value())
                    .message("Login failed, username not found!")
                    .build();
        }




//        return null;
    }

    public AuthenticationResponseDTO refreshToken(String authHeader) throws CustomException {
        if (StringUtils.hasText(authHeader)|| !authHeader.startsWith("Bearer ")) {
            return  AuthenticationResponseDTO.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthorized!")
                    .build();
        }
        //fix hard code
        String refreshToken = authHeader.substring(TOKEN_INDEX);

        //get username from refreshToken
        String userName = jwtService.extractUsername(refreshToken);

        if(StringUtils.hasText(userName)){
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"Username is empty");
        }

        //Get user data from database
        Optional<User> userFoundByUsername = userRepository.findByUsername(userName);
        if(userFoundByUsername.isPresent()) {
            throw new UsernameNotFoundException(userName);
        }

        User user = userFoundByUsername.get();
        if (StringUtils.hasText(user.getAccessToken())|| !StringUtils.hasText(user.getRefreshToken())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Token of the user revoked");
        }
        //generate access token and refresh token to client
        String accessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        user.setAccessToken(accessToken);
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        //response access token and refresh token to client
        return AuthenticationResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Refresh token successfully")
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

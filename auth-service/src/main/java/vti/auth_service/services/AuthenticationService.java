package vti.auth_service.services;

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
    private final AuthenticationManager authenTicationManager;
    private final JwtService jwtService;

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        //Check email. username is existed in db
        String email = registerRequestDTO.getEmail();
        String userName = registerRequestDTO.getUsername();

        Optional<User> userFoundByEmail = userRepository.findByEmail(email);
        Optional<User> userFoundByUsername = userRepository.findByUsername(userName);

        if (userFoundByEmail.isPresent() || userFoundByUsername.isPresent()) {
            throw new ConstraintViolationException("User already exists!", null);
        }

        //create user and save user into db
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
        //Authentication username and password
//        log.info("User request body login:");
//        log.info("Login request dto: {}", loginRequestDTO);
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();
        authenTicationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));


        //Generate access token and refresh token
        Optional<User> userFoundByUsername = userRepository.findByUsername(username);
        if (userFoundByUsername.isPresent()) {
            User user = userFoundByUsername.get();
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            //Save access token and refresh token into user in database
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            return AuthenticationResponseDTO.builder()
                    .status(HttpStatus.OK.value())
                    .message("Login successfully")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userId(user.getId())
                    .build();
        } else {
            return AuthenticationResponseDTO.builder()
                    .status(HttpStatus.FORBIDDEN.value())
                    .message("Login failed, username not found")
                    .build();
        }
        //save access token and refresh token into user in database

        //response access token and refresh token to client

    }

    public AuthenticationResponseDTO refreshToken(String authHeader) throws CustomException {
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer")) {
            return AuthenticationResponseDTO.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthorized")
                    .build();
        }

        // fix hard code
        String refreshToken = authHeader.substring(TOKEN_INDEX);

        // Get username from refreshToken
        String userName = jwtService.extractUsername(refreshToken);
        if (!StringUtils.hasText(userName)) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Username is empty");
        }
        // Get User data from database
        Optional<User> userFoundByUsername = userRepository.findByUsername(userName);
        if (userFoundByUsername.isEmpty()) {
            throw new UsernameNotFoundException(userName);
        }

        User user = userFoundByUsername.get();
        if (!StringUtils.hasText(user.getAccessToken()) || !StringUtils.hasText(user.getRefreshToken())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Token of the user revoked");
        }

        // Gerenate access token and refresh token
        String accessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        user.setAccessToken(accessToken);
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);
        // Response access token and refresh token to client
        return AuthenticationResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Refresh token successfully")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();

    }
}

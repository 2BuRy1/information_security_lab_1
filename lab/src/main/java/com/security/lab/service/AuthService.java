package com.security.lab.service;

import com.security.lab.dto.LoginRequestDTO;
import com.security.lab.dto.LoginResponseDTO;
import com.security.lab.dto.RegisterRequestDTO;
import com.security.lab.dto.RegisterResponseDTO;
import com.security.lab.entity.User;
import com.security.lab.exception.PasswordMismatchException;
import com.security.lab.exception.UserAlreadyExistsException;
import com.security.lab.repository.UserRepository;
import java.util.Objects;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        var authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDTO.login(), loginRequestDTO.password()));
        var user = (User) authentication.getPrincipal();
        return new LoginResponseDTO(jwtService.generateToken(user));
    }

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findUsersByLogin(registerRequestDTO.login()).isPresent()) {
            throw new UserAlreadyExistsException(registerRequestDTO.login());
        }
        if (!Objects.equals(registerRequestDTO.password(), registerRequestDTO.repeatedPassword())) {
            throw new PasswordMismatchException();
        }

        var user =
                userRepository.save(
                        new User(
                                registerRequestDTO.login(),
                                passwordEncoder.encode(registerRequestDTO.password())));
        return new RegisterResponseDTO(jwtService.generateToken(user));
    }
}

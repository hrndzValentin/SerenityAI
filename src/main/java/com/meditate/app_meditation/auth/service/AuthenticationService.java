package com.meditate.app_meditation.auth.service;

import com.meditate.app_meditation.auth.dto.AuthenticationRequest;
import com.meditate.app_meditation.auth.dto.AuthenticationResponse;
import com.meditate.app_meditation.auth.dto.RegisterRequest;
import com.meditate.app_meditation.user.entity.User;
import com.meditate.app_meditation.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .fullName(request.firstName() + " " + request.lastName())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .subscriptionStatus(User.SubscriptionStatus.FREE)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken, null);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = userRepository.findByEmail(request.email())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken, null);
    }
}

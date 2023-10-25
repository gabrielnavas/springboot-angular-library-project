package com.library.api.user.auth;

import com.library.api.exceptions.ObjectAlreadyExistsWithException;
import com.library.api.user.*;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    public UserResponse register(UserRequest request) {
        Optional<User> optionalUserByEmail = userRepository.findByEmail(request.getEmail());
        if (optionalUserByEmail.isPresent()) {
            throw new ObjectAlreadyExistsWithException("user", "email", request.getEmail());
        }

        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            throw new RuntimeException("password is different from password confirmation");
        }
        final Integer reservationLimit = 5;
        final Integer borrowingLimit = 5;
        final Date now = new Date();
        final String password = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .reservationLimit(reservationLimit)
                .borrowingLimit(borrowingLimit)
                .email(request.getEmail())
                .password(password)
                .role(Role.CLIENT)
                .telephone(request.getTelephone())
                .streetNumber(request.getStreetNumber())
                .streetName(request.getStreetName())
                .createdAt(now)
                .updatedAt(now)
                .build();

        user = userRepository.save(user);

        final String jwtToken = jwtService.generateToken(user);

        return UserResponse.modelToResponse(user, jwtToken);
    }

    public UserResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("user not found");
        }
        var jwtToken = jwtService.generateToken(optionalUser.get());
        return UserResponse.modelToResponse(optionalUser.get(), jwtToken);
    }
}

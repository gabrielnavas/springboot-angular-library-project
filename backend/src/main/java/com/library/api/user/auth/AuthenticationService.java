package com.library.api.user.auth;

import com.library.api.user.Role;
import com.library.api.user.User;
import com.library.api.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public AuthenticationResponse register(AuthenticationRequest request) {
        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            throw new RuntimeException("password is different from password confirmation");
        }
        final Integer reservationLimit = 5;
        final Integer borrowingLimit = 5;
        final Date now = new Date();
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .reservationLimit(reservationLimit)
                .borrowingLimit(borrowingLimit)
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Role.CLIENT)
                .telephone(request.getTelephone())
                .streetNumber(request.getStreetNumber())
                .streetName(request.getStreetName())
                .createdAt(now)
                .updatedAt(now)
                .build();

        user = userRepository.save(user);

        final String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.modelToResponse(user, jwtToken);
    }
}

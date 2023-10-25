package com.library.api.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String email;
    private Role role;
    private String firstName;
    private String lastName;
    private String streetName;
    private String streetNumber;
    private String telephone;
    private String token;
    private Date createdAt;
    private Date updatedAt;

    public static UserResponse modelToResponse(User user, String token) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .streetName(user.getStreetName())
                .streetNumber(user.getStreetNumber())
                .telephone(user.getTelephone())
                .token(token)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

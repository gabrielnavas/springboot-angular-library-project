package com.library.api.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String streetName;
    private String streetNumber;
    private String telephone;
    private String password;
    private String passwordConfirmation;
}

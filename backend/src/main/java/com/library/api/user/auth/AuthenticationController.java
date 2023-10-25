package com.library.api.user.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/auth")
@AllArgsConstructor
@Tag(name = "Authentication", description = "Endpoints to Managing the Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Register an user",
            description = "Endpoint to register an user",
            tags = {"PublishingCompany"},
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "204",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))
                    ),
                    @ApiResponse(
                            description = "BadRequest",
                            responseCode = "400",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "InternalServerError",
                            responseCode = "500",
                            content = @Content
                    )
            }
    )
    @PostMapping(
            value = "/register",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> register(
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        AuthenticationResponse authenticationResponse = authenticationService.register(authenticationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);
    }
}

package com.loievroman.bookstoreapp.controller;

import com.loievroman.bookstoreapp.dto.user.UserLoginRequestDto;
import com.loievroman.bookstoreapp.dto.user.UserLoginResponseDto;
import com.loievroman.bookstoreapp.dto.user.UserRegistrationRequestDto;
import com.loievroman.bookstoreapp.dto.user.UserResponseDto;
import com.loievroman.bookstoreapp.exception.RegistrationException;
import com.loievroman.bookstoreapp.security.AuthenticationService;
import com.loievroman.bookstoreapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Register a new user",
            description = "This endpoint allows to create a new user in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data"),
            @ApiResponse(responseCode = "409", description = "User already exists"),
    })
    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }

    @Operation(
            summary = "Authenticate user and return JWT token",
            description = "This endpoint allows to authenticate a "
                    + "user and obtain a JWT token for subsequent API calls."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                    content = @Content(
                            schema = @Schema(implementation = UserLoginResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        return authenticationService.authenticate(userLoginRequestDto);
    }
}

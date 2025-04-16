package com.loievroman.bookstoreapp.dto.user;

import com.loievroman.bookstoreapp.validation.fieldmatch.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch(first = "password", second = "repeatPassword", message = "Passwords do not match")
public class UserRegistrationRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String repeatPassword;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String shippingAddress;

}

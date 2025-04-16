package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.user.UserRegistrationRequestDto;
import com.loievroman.bookstoreapp.dto.user.UserResponseDto;
import com.loievroman.bookstoreapp.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}

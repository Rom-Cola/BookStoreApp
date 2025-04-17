package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.user.UserRegistrationRequestDto;
import com.loievroman.bookstoreapp.dto.user.UserResponseDto;
import com.loievroman.bookstoreapp.exception.RegistrationException;
import com.loievroman.bookstoreapp.mapper.UserMapper;
import com.loievroman.bookstoreapp.model.User;
import com.loievroman.bookstoreapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException(
                    "Can't register user. User with email=%s already registered."
                            .formatted(requestDto.getEmail())
            );
        }
        User user = userMapper.toModel(requestDto);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}

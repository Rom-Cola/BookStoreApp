package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.user.UserRegistrationRequestDto;
import com.loievroman.bookstoreapp.dto.user.UserResponseDto;
import com.loievroman.bookstoreapp.exception.EntityNotFoundException;
import com.loievroman.bookstoreapp.exception.RegistrationException;
import com.loievroman.bookstoreapp.mapper.UserMapper;
import com.loievroman.bookstoreapp.model.Role;
import com.loievroman.bookstoreapp.model.User;
import com.loievroman.bookstoreapp.repository.RoleRepository;
import com.loievroman.bookstoreapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException(
                    "Can't register user. User with email=%s already registered."
                            .formatted(requestDto.getEmail())
            );
        }
        User user = userMapper.toEntity(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Role userRole = roleRepository.findByRole(Role.RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Default role "
                                + Role.RoleName.USER.name()
                                + "not found in the database"));

        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        shoppingCartService.createUserShoppingCart(user);

        return userMapper.toUserResponse(user);
    }
}

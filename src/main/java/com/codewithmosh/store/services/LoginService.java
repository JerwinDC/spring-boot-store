package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.LoginRequest;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.exceptions.InvalidCredentialsException;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findUser(LoginRequest request){

        var user= userRepository.findByEmail(request.getEmail()).orElse(null);
        if(user == null){
            throw new InvalidCredentialsException();
        } else if (!(passwordEncoder.matches(user.getEmail(), request.getEmail()))) {
            throw new InvalidCredentialsException();
        }

        return user;

    }
}

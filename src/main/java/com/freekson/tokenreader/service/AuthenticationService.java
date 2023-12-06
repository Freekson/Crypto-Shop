package com.freekson.tokenreader.service;

import com.freekson.tokenreader.entity.User;
import com.freekson.tokenreader.model.LoginUserDto;
import com.freekson.tokenreader.model.RegisterUserDto;
import com.freekson.tokenreader.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User signup(RegisterUserDto user){
        User newUser = new User();
        newUser.setLogin(user.getLogin());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(newUser);
    }

    public User authenticate(LoginUserDto user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getLogin(),
                        user.getPassword()
                )
        );

        return userRepository.findByLogin(user.getLogin())
                .orElseThrow();
    }
}

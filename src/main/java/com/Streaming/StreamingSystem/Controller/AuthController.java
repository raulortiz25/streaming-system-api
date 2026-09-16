package com.Streaming.StreamingSystem.Controller;

import com.Streaming.StreamingSystem.DTO.AuthCreateUserRequest;
import com.Streaming.StreamingSystem.DTO.AuthLoginRequest;
import com.Streaming.StreamingSystem.DTO.AuthResponse;
import com.Streaming.StreamingSystem.DTO.ChangePlanRequest;
import com.Streaming.StreamingSystem.Security.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/auth")
public class AuthController {
    @Autowired
    private UserDetailsServiceImpl user;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse>registrarse(@Valid @RequestBody AuthCreateUserRequest userRequest){
        return new ResponseEntity<>(this.user.createUser(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse>iniciarSesion(@Valid @RequestBody AuthLoginRequest loginRequest){
        return new ResponseEntity<>(this.user.loginUser(loginRequest),HttpStatus.OK );
    }

    @PatchMapping("/change")
    public ResponseEntity<AuthResponse>changePlan(@Valid @RequestBody ChangePlanRequest planRequest, Authentication authentication){

        String username = authentication.getName();
        return new ResponseEntity<>(this.user.changePlan( username, planRequest),HttpStatus.OK );
    }





}

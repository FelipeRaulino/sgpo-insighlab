package com.insightlab.sgpo.controllers;

import com.insightlab.sgpo.data.dtos.v1.security.AuthenticationRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.security.TokenResponseDTO;
import com.insightlab.sgpo.data.dtos.v1.security.UserRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.security.UserResponseDTO;
import com.insightlab.sgpo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequest){

        UserResponseDTO userResponseDTO = this.userService.createUser(userRequest);

        return ResponseEntity.ok().body(userResponseDTO);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(
            @RequestBody AuthenticationRequestDTO authenticationRequest
    ){
        if (checkCredentials(authenticationRequest))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid user request");

        TokenResponseDTO token = this.userService.authenticateUser(authenticationRequest);

        if (token == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid user request");

        return ResponseEntity.ok().body(token);
    }

    private boolean checkCredentials(AuthenticationRequestDTO authenticationRequest) {
        return (authenticationRequest == null || authenticationRequest.username() == null
                || authenticationRequest.username().isBlank()
                || authenticationRequest.password() == null
                || authenticationRequest.password().isBlank());
    }

}

package com.insightlab.sgpo.services;

import com.insightlab.sgpo.data.dtos.v1.security.AuthenticationRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.security.TokenResponseDTO;
import com.insightlab.sgpo.data.dtos.v1.security.UserRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.security.UserResponseDTO;
import com.insightlab.sgpo.domain.security.Role;
import com.insightlab.sgpo.domain.security.User;
import com.insightlab.sgpo.domain.security.enums.ERole;
import com.insightlab.sgpo.repositories.RoleRepository;
import com.insightlab.sgpo.repositories.UserRepository;
import com.insightlab.sgpo.security.jwt.JWTProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager,
                       @Lazy JWTProvider jwtProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequest){
        if (this.userRepository.existsByUsername(userRequest.username())){
            throw new RuntimeException("Username already exists!");
        }

        User user = new User();

        Set<String> strRoles = userRequest.roles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByType(ERole.ROLE_EMPLOYEE)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleRepository.findByType(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByType(ERole.ROLE_EMPLOYEE)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        user.setUsername(userRequest.username());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setRoles(roles);

        this.userRepository.save(user);

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword()
        );
    }

    public TokenResponseDTO authenticateUser(AuthenticationRequestDTO authenticationRequestDTO){
        String username = authenticationRequestDTO.username();
        String password = authenticationRequestDTO.password();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = this.userRepository.findByUsername(username);

        TokenResponseDTO tokenResponseDTO;

        if (user != null) {
            tokenResponseDTO = jwtProvider.createAccessToken(username, user.getRoles());
        } else {
            throw new UsernameNotFoundException("Username " + username + "not found!");
        }

        return tokenResponseDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);

        if (user != null){
            return user;
        } else {
            throw new UsernameNotFoundException("Username: " + username + "was not found!");
        }

    }
}

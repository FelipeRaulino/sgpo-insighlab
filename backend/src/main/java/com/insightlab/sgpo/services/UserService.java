package com.insightlab.sgpo.services;

import com.insightlab.sgpo.data.dtos.v1.security.*;
import com.insightlab.sgpo.domain.security.Role;
import com.insightlab.sgpo.domain.security.User;
import com.insightlab.sgpo.domain.security.enums.ERole;
import com.insightlab.sgpo.domain.security.exceptions.UserIdNotFoundException;
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

    public List<UserResponseDTO> getAllUsers(){
        List<User> usersList = this.userRepository.findAll();

        return usersList.stream().map(user -> {
            return new UserResponseDTO(
                    user.getId(),
                    user.getUsername(),
                    user.isAccountNonExpired(),
                    user.isAccountNonLocked(),
                    user.isCredentialsNonExpired(),
                    user.isEnabled(),
                    convertRolesToRoleNames(user.getRoles())
            );
        }).toList();
    }

    public void deleteUser(String id){
        User userDB = this.userRepository.findById(id)
                .orElseThrow(() -> new UserIdNotFoundException("User with id: " + id + " was not found!"));

        this.userRepository.delete(userDB);
    }

    public CreateUserResponseDTO createUser(CreateUserRequestDTO userRequest){
        if (this.userRepository.existsByUsername(userRequest.username())){
            throw new RuntimeException("Username already exists!");
        }

        User user = new User();

        List<String> strRoles = userRequest.roles();
        List<Role> roles = convertRoleNamesToRoles(strRoles);

        user.setUsername(userRequest.username());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setRoles(roles);

        this.userRepository.save(user);

        return new CreateUserResponseDTO(
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
            tokenResponseDTO = jwtProvider.createAccessToken(user.getId(), username, user.getRoles());
        } else {
            throw new UsernameNotFoundException("Username " + username + "not found!");
        }

        return tokenResponseDTO;
    }

    public UserUpdateResponseDTO updateUser(String id, UserUpdateRequestDTO userUpdateRequest){
        User userDB = this.userRepository.findById(id)
                .orElseThrow(() -> new UserIdNotFoundException("User with id: " + id + " was not found!"));

        List<Role> roles = convertRoleNamesToRoles(userUpdateRequest.roles());

        userDB.setUsername(userUpdateRequest.username());
        userDB.setEnabled(true);
        userDB.setCredentialsNonExpired(userDB.isAccountNonExpired());
        userDB.setAccountNonLocked(userDB.isAccountNonLocked());
        userDB.setPassword(userDB.getPassword());
        userDB.setRoles(roles);

        this.userRepository.save(userDB);

        TokenResponseDTO tokenResponseDTO = jwtProvider.createAccessToken(
                userDB.getId(),
                userDB.getUsername(),
                userDB.getRoles()
        );

        return new UserUpdateResponseDTO(
                userDB.getId(),
                userDB.getUsername(),
                convertRolesToRoleNames(userDB.getRoles()),
                tokenResponseDTO.accessToken()
        );
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

    private List<Role> convertRoleNamesToRoles(List<String> rolesStringList){
        List<Role> roles = new ArrayList<>();

        if (rolesStringList == null) {
            Role userRole = roleRepository.findByType(ERole.ROLE_EMPLOYEE)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            rolesStringList.forEach(role -> {
                if (role.equals("ROLE_ADMIN")) {
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

        return roles;
    }

    private List<String> convertRolesToRoleNames(List<Role> roles){
        List<String> roleNames = new ArrayList<>();

        if (roles == null || roles.isEmpty()){
            roleNames.add("ROLE_EMPLOYEE");
        } else {
            roles.forEach(role -> {
                ERole roleType = role.getType();
                roleNames.add(roleType.name());
            });
        }

        return roleNames;
    }
}

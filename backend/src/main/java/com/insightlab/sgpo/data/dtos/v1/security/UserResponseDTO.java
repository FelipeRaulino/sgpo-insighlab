package com.insightlab.sgpo.data.dtos.v1.security;

import java.util.List;

public record UserResponseDTO(
        String id,
        String username,
        Boolean accountNonExpired,
        Boolean accountNonLocked,
        Boolean credentialsNonExpired,
        Boolean enabled,
        List<String> roles
){}

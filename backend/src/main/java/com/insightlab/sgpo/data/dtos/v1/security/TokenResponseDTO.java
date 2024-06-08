package com.insightlab.sgpo.data.dtos.v1.security;

import java.util.Date;
import java.util.List;

public record TokenResponseDTO(
        String id,
        String username,
        Boolean authenticated,
        Date created,
        Date expiration,
        List<String> roles,
        String accessToken
){}

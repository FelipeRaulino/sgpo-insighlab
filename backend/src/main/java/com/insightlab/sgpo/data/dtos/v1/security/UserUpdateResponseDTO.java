package com.insightlab.sgpo.data.dtos.v1.security;

import java.util.List;

public record UserUpdateResponseDTO(String id, String username, List<String> roles, String token) {
}

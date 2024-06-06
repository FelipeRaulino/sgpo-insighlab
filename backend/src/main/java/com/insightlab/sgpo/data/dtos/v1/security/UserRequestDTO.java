package com.insightlab.sgpo.data.dtos.v1.security;

import java.util.Set;

public record UserRequestDTO(String username, String password, Set<String> roles) {
}

package com.insightlab.sgpo.data.dtos.v1.security;

import java.util.List;

public record UserUpdateRequestDTO(String username, List<String> roles) {
}

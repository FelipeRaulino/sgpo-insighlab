package com.insightlab.sgpo.data.dtos.v1.security;


import java.util.List;

public record CreateUserRequestDTO(String username, String password, List<String> roles) {
}

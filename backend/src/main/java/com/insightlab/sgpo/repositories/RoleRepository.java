package com.insightlab.sgpo.repositories;

import com.insightlab.sgpo.domain.security.Role;
import com.insightlab.sgpo.domain.security.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByType(ERole type);
}

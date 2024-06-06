package com.insightlab.sgpo.domain.security;

import com.insightlab.sgpo.domain.security.enums.ERole;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "role")
public class Role implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private ERole type;

    public Role() {}

    public Role(ERole type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ERole getType() {
        return type;
    }

    public void setType(ERole type) {
        this.type = type;
    }
}

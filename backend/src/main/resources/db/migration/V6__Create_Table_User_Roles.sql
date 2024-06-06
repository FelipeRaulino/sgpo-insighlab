CREATE TABLE user_roles (
    id_user varchar(255) NOT NULL,
    id_role varchar(255) NOT NULL,
    PRIMARY KEY (id_user, id_role),
    FOREIGN KEY (id_user) REFERENCES "user"(id),
    FOREIGN KEY (id_role) REFERENCES role(id)
);
CREATE TABLE supplier (
    id varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    tax_id varchar(24) NOT NULL,
    phone varchar(24) NOT NULL,
    email varchar(255) NOT NULL,
    status boolean NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
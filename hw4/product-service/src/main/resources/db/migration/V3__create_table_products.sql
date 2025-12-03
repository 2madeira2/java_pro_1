CREATE TABLE products (
     id BIGSERIAL PRIMARY KEY,
     account_number VARCHAR(20) NOT NULL,
     balance DECIMAL NOT NULL DEFAULT 0.00,
     type VARCHAR(20) NOT NULL,
     user_id BIGINT NOT NULL,
     CONSTRAINT fk_user_products FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

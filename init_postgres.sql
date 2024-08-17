CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    balance BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    amount BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    account_id BIGINT NOT NULL,
    CONSTRAINT fk_account
        FOREIGN KEY (account_id)
        REFERENCES accounts(id)
);

CREATE INDEX idx_transaction_account_id ON transactions(account_id);
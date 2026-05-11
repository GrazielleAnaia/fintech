CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uk_user_currency UNIQUE (user_id, currency));

CREATE TABLE ledger_entries(
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    transaction_id UUID NOT NULL,
    reference_id UUID NOT NULL,
    amount NUMERIC(19,4) NOT NULL,
    ledger_type VARCHAR(10) NOT NULL,
    created_at TIMESTAMP,
CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts(id));

CREATE INDEX idx_account_id ON ledger_entries(account_id);
CREATE INDEX idx_transaction_id ON ledger_entries(transaction_id);
CREATE UNIQUE INDEX uk_reference_id ON ledger_entries(reference_id);
CREATE UNIQUE INDEX uk_reference_account ON ledger_entries(reference_id, account_id);

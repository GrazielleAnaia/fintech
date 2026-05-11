--Accounts
INSERT INTO accounts(id, user_id, currency, created_at)
VALUES ('11111111-1111-1111-1111-111111111111', 1, 'USD', CURRENT_TIMESTAMP),
       ('22222222-2222-2222-2222-222222222222', 2, 'USD', CURRENT_TIMESTAMP);

--Ledger_entries simulate a deposit
INSERT INTO ledger_entries(
                           id, account_id, transaction_id, reference_id, amount, ledger_type, created_at)
    VALUES('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
           '11111111-1111-1111-1111-111111111111',
           'bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
           'ccccccc1-cccc-cccc-cccc-ccccccccccc1',
           1000.00,
           'CREDIT',
           CURRENT_TIMESTAMP
);
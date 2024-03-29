create table tariff
(
    id            uuid primary key,
    addition_date timestamp     not null,
    name          varchar(512)  not null,
    description   varchar(2048) not null,
    interest_rate decimal(5, 2) not null,
    officer_id    uuid          not null
);

create table loan_application
(
    id                 uuid primary key,
    creation_date     timestamp       not null,
    updated_date_final timestamp null,
    loan_term_in_days int             not null,
    issued_amount     decimal(100, 2) not null,
    currency          varchar(64)     not null,
    state             varchar(64)     not null,
    tariff_id         uuid            not null,
    client_id         uuid            not null,
    officer_id         uuid,
    foreign key (tariff_id) references tariff (id) on delete restrict on update restrict
);

create table loan
(
    id                  uuid primary key,
    issued_date         timestamp       not null,
    repayment_date      timestamp       not null,
    issued_amount       decimal(100, 2) not null,
    amount_loan         decimal(100, 2) not null,
    amount_debt         decimal(100, 2) not null,
    accrued_penny       decimal(100, 2) not null,
    currency            varchar(64)     not null,
    loan_term_in_days   int             not null,
    state               varchar(64)     not null,
    tariff_id           uuid            not null,
    loan_application_id uuid            not null,
    client_id           uuid            not null,
    account_id          uuid            not null,
    foreign key (tariff_id) references tariff (id) on delete restrict on update restrict,
    foreign key (loan_application_id) references loan_application (id) on delete restrict on update restrict
);

create table loan_repayment
(
    id       uuid primary key,
    date     timestamp       not null,
    amount   decimal(100, 2) not null,
    currency varchar(64)     not null,
    state    varchar(64)     not null,
    loan_id  uuid            not null,
    foreign key (loan_id) references loan (id) on delete restrict on update restrict
);

create table internal_transaction_ids
(
    loan_repayment_id uuid references loan_repayment (id),
    transaction_id    uuid unique
);


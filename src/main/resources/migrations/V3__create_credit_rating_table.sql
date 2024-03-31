create table credit_rating
(
    id               uuid primary key,
    client_id        uuid      not null,
    calculation_date timestamp not null,
    rating           int       not null
);
--liquibase formatted sql
--changeset author:1

create table authors(
    id bigserial primary key,
    name varchar(255) not null unique
);

create table genres(
    id bigserial primary key,
    name varchar(255) not null unique
);

create table books(
    id bigserial primary key,
    title varchar(255) not null unique,
    description varchar(2048),
    time timestamp,
    author_id bigint references authors(id) on delete set null,
    score_one_count integer default 0,
    score_two_count integer default 0,
    score_three_count integer default 0,
    score_four_count integer default 0,
    score_five_count integer default 0,
    comment_count integer default 0,
    avg_score double precision
);

create table roles(
    id bigserial primary key,
    role varchar(255)
);

create table permissions(
    id bigserial primary key,
    permission varchar(255),
    role_id bigint references roles(id) on delete set null
);

create table users(
    id bigserial primary key,
    email varchar(255) not null unique,
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255),
    is_enabled boolean,
    role_id bigint references roles(id) on delete set null
);

create table comments(
    id bigserial primary key,
    book_id bigint references books(id) on delete set null,
    user_id bigint references users(id) on delete set null,
    time timestamp,
    text varchar(2048) not null
);

create table books_to_genres(
    book_id bigint references books(id) on delete set null,
    genre_id bigint references genres(id) on delete set null
);

create table scores(
    id bigserial primary key,
    book_id bigint references books(id) on delete set null,
    user_id bigint references users(id) on delete set null,
    score integer not null
);





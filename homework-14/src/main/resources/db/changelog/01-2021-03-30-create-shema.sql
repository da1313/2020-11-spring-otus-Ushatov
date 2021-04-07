--liquibase formatted sql
--changeset author:1
create sequence book_id_seq start 1;
create sequence author_id_seq start 1;
create sequence genre_id_seq start 1;
create sequence comment_id_seq start 1;
create sequence score_id_seq start 1;
create sequence user_id_seq start 1;

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
    time timestamp,
    description varchar(2048),
    author_id bigint references authors(id) on delete set null,
    score_one_count integer default 0,
    score_two_count integer default 0,
    score_three_count integer default 0,
    score_four_count integer default 0,
    score_five_count integer default 0,
    comment_count integer default 0,
    avg_score double precision
);

create table users(
    id bigserial primary key,
    name varchar(255),
    password varchar(255),
    is_active boolean
);

create table comments(
    id bigserial primary key,
    text varchar(2048) not null,
    time timestamp,
    book_id bigint references books(id) on delete set null,
    user_id bigint references users(id) on delete set null
);

create table books_to_genres(
    book_id bigint references books(id) on delete set null,
    genre_id bigint references genres(id) on delete set null
);

create table scores(
    id bigserial primary key,
    score integer not null,
    book_id bigint references books(id) on delete set null,
    user_id bigint references users(id) on delete set null

);





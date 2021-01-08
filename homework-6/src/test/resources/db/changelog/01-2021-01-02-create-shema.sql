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
    name varchar(255) not null unique,
    author_id bigint references authors(id) on delete set null
);

create table book_comments(
    id bigserial primary key,
    book_id bigint references books(id) on delete set null,
    text varchar(2048) not null
);

create table books_to_genres(
    book_id bigint references books(id) on delete set null,
    genre_id bigint references genres(id) on delete set null
);
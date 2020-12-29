--liquibase formatted sql
--changeset author:1
create table books(
    id bigint primary key auto_increment not null,
    name varchar(255) not null,
    author_id bigint,
    genre_id bigint
);

create table authors(
    id bigint primary key auto_increment not null,
    name varchar(255) not null
);

create table genres(
    id bigint primary key auto_increment not null,
    name varchar(255) not null
);

create table categories(
    id bigint primary key auto_increment not null,
    name varchar(255) not null
);

create table books_to_categories(
    book_id bigint,
    category_id bigint
);

alter table books add constraint author_fk foreign key (author_id) references authors(id) on delete set null;

alter table books add constraint genre_fk foreign key (genre_id) references genres(id) on delete set null;

alter table books_to_categories add constraint book_fk foreign key (book_id) references books(id) on delete set null;

alter table books_to_categories add constraint category_fk foreign key (category_id) references categories(id) on delete set null;
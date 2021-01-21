--liquibase formatted sql
--changeset author:2
insert into authors(name) values ('A1');
insert into authors(name) values ('A2');
insert into genres(name) values ('G1');
insert into genres(name) values ('G2');
insert into genres(name) values ('G3');
insert into books(name, author_id) values ('B1', 1);
insert into books(name, author_id) values ('B2', 1);
insert into books(name, author_id) values ('B3', 1);
insert into books(name, author_id) values ('B4', 2);
insert into books(name, author_id) values ('B5', 2);
insert into books(name, author_id) values ('B6', 2);
insert into books_to_genres(book_id, genre_id) values (1, 1);
insert into books_to_genres(book_id, genre_id) values (1, 2);
insert into books_to_genres(book_id, genre_id) values (1, 3);
insert into books_to_genres(book_id, genre_id) values (2, 1);
insert into books_to_genres(book_id, genre_id) values (2, 2);
insert into books_to_genres(book_id, genre_id) values (3, 3);
insert into book_comments(book_id, text) values(1, 'C1');
insert into book_comments(book_id, text) values(1, 'C2');
insert into book_comments(book_id, text) values(2, 'C3');
insert into book_comments(book_id, text) values(3, 'C4');
insert into book_comments(book_id, text) values(3, 'C5');
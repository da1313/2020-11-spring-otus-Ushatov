--liquibase formatted sql
--changeset author:3
insert into authors(name) values ('A1');
insert into authors(name) values ('A2');
insert into authors(name) values ('A3');
insert into authors(name) values ('A4');
insert into genres(name) values ('G1');
insert into genres(name) values ('G2');
insert into genres(name) values ('G3');
insert into genres(name) values ('G4');
insert into genres(name) values ('G5');
insert into users(name) values ('U1');
insert into users(name) values ('U2');
insert into users(name) values ('U3');
insert into books(title, description, time, author_id) values ('B1', 'D1', now(), 1);
insert into books(title, description, time, author_id) values ('B2', 'D2', now(), 1);
insert into books(title, description, time, author_id) values ('B3', 'D3', now(), 1);
insert into books(title, description, time, author_id) values ('B4', 'D4', now(), 2);
insert into books(title, description, time, author_id) values ('B5', 'D5', now(), 2);
insert into books(title, description, time, author_id) values ('B6', 'D6', now(), 3);
insert into books(title, description, time, author_id) values ('B7', 'D7', now(), 3);
insert into books(title, description, time, author_id) values ('B8', 'D8', now(), 4);
insert into books(title, description, time, author_id) values ('B9', 'D9', now(), 4);
insert into books(title, description, time, author_id) values ('B10', 'D10', now(), 1);
insert into books(title, description, time, author_id) values ('B11', 'D11', now(), 1);
insert into books(title, description, time, author_id) values ('B12', 'D12', now(), 3);
insert into books(title, description, time, author_id) values ('B13', 'D13', now(), 2);
insert into books(title, description, time, author_id) values ('B14', 'D14', now(), 2);
insert into books(title, description, time, author_id) values ('B15', 'D15', now(), 1);
insert into books(title, description, time, author_id) values ('B16', 'D16', now(), 4);
insert into books(title, description, time, author_id) values ('B17', 'D17', now(), 3);
insert into books_to_genres(book_id, genre_id) values (1, 1);
insert into books_to_genres(book_id, genre_id) values (1, 2);
insert into books_to_genres(book_id, genre_id) values (1, 3);
insert into books_to_genres(book_id, genre_id) values (2, 1);
insert into books_to_genres(book_id, genre_id) values (2, 2);
insert into books_to_genres(book_id, genre_id) values (3, 1);
insert into books_to_genres(book_id, genre_id) values (3, 3);
insert into books_to_genres(book_id, genre_id) values (4, 1);
insert into books_to_genres(book_id, genre_id) values (4, 4);
insert into books_to_genres(book_id, genre_id) values (5, 1);
insert into books_to_genres(book_id, genre_id) values (5, 2);
insert into books_to_genres(book_id, genre_id) values (6, 1);
insert into books_to_genres(book_id, genre_id) values (7, 3);
insert into books_to_genres(book_id, genre_id) values (8, 4);
insert into comments(book_id, user_id, text) values(4, 1, 'C1');
insert into comments(book_id, user_id, text) values(4, 1, 'C2');
insert into comments(book_id, user_id, text) values(4, 2, 'C3');
insert into comments(book_id, user_id, text) values(4, 2, 'C4');
insert into comments(book_id, user_id, text) values(4, 1, 'C5');
insert into comments(book_id, user_id, text) values(2, 1, 'C6');
insert into comments(book_id, user_id, text) values(2, 2, 'C7');
insert into comments(book_id, user_id, text) values(2, 3, 'C8');
insert into comments(book_id, user_id, text) values(2, 2, 'C9');
insert into comments(book_id, user_id, text) values(3, 3, 'C10');
insert into comments(book_id, user_id, text) values(3, 1, 'C11');
insert into comments(book_id, user_id, text) values(7, 1, 'C12');
insert into comments(book_id, user_id, text) values(4, 2, 'C13');
insert into comments(book_id, user_id, text) values(4, 1, 'C14');
insert into comments(book_id, user_id, text) values(5, 1, 'C15');
insert into comments(book_id, user_id, text) values(5, 2, 'C16');
insert into comments(book_id, user_id, text) values(5, 3, 'C17');
insert into scores(book_id, user_id, score) values(1, 1, 2);
insert into scores(book_id, user_id, score) values(1, 2, 3);
insert into scores(book_id, user_id, score) values(2, 3, 4);
insert into scores(book_id, user_id, score) values(2, 1, 1);
insert into scores(book_id, user_id, score) values(2, 2, 5);
insert into scores(book_id, user_id, score) values(3, 3, 2);
insert into scores(book_id, user_id, score) values(3, 1, 2);
insert into scores(book_id, user_id, score) values(3, 2, 5);
insert into scores(book_id, user_id, score) values(3, 3, 2);
insert into scores(book_id, user_id, score) values(4, 3, 3);
insert into scores(book_id, user_id, score) values(4, 3, 4);
insert into scores(book_id, user_id, score) values(4, 3, 2);
insert into scores(book_id, user_id, score) values(4, 3, 3);
insert into scores(book_id, user_id, score) values(7, 3, 5);
insert into scores(book_id, user_id, score) values(7, 3, 5);
insert into scores(book_id, user_id, score) values(8, 3, 1);
insert into scores(book_id, user_id, score) values(8, 3, 2);
insert into scores(book_id, user_id, score) values(5, 3, 3);
insert into scores(book_id, user_id, score) values(5, 3, 5);
insert into scores(book_id, user_id, score) values(5, 3, 2);
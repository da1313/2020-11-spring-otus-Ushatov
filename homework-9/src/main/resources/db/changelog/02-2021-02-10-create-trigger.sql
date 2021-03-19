--liquibase formatted sql
--changeset author:2

create or replace function update_book_info_score() returns trigger
language plpgsql
as '
declare
begin
    if(new.score = 1) then
        update books set score_one_count = score_one_count + 1 where id = new.book_id;
    elsif(new.score = 2) then
        update books set score_two_count = score_two_count + 1 where id = new.book_id;
    elsif(new.score = 3) then
        update books set score_three_count = score_three_count + 1 where id = new.book_id;
    elsif(new.score = 4) then
        update books set score_four_count = score_four_count + 1 where id = new.book_id;
    elsif(new.score = 5) then
        update books set score_five_count = score_five_count + 1 where id = new.book_id;
    end if;
	return new;
end;
';

create or replace function update_book_info_comment() returns trigger
language plpgsql
as '
declare
begin
    update books set comment_count = comment_count + 1 where id = new.book_id;
	return new;
end;
';

create trigger add_score_trigger
after insert
on scores
for each row
execute procedure update_book_info_score();

create trigger add_comment_count_trigger
after insert
on comments
for each row
execute procedure update_book_info_comment();


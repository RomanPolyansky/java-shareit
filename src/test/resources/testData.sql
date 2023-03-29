insert into users (id, name, email)
values (1, 'test', 'test@email.ru');

insert into item_requests (id, description, requester_id, created)
values (1, 'test', 1, now());

insert into items (id, name, description, is_available, owner_id, request_id)
values (1, 'test', 'test description', true, 1, 1);

insert into comments(id, text, author_id, item_id, created)
values (1, 'test', 1, 1, now());

insert into bookings (id, start_date, end_date, booker_id, item_id, status)
values (1, now(), TIMESTAMP '2022-12-31 23.59.59', 1, 1, 'waiting');
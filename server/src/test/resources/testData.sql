insert into users (id, name, email)
values (100, 'test', 'test@email.ru');
insert into users (id, name, email)
values (200, 'test2', 'test2@email.ru');

insert into item_requests (id, description, requester_id, created)
values (100, 'test', 100, now());

insert into items (id, name, description, is_available, owner_id, request_id)
values (100, 'test', 'test description', true, 100, 100);

insert into comments(id, text, author_id, item_id, created)
values (100, 'test', 100, 100, now());

insert into bookings (id, start_date, end_date, booker_id, item_id, status)
values (100, now(), TIMESTAMP '2025-12-31 23.59.59', 100, 100, 'waiting');
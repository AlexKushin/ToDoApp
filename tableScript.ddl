DROP SCHEMA IF EXISTS todo_app CASCADE;

CREATE SCHEMA todo_app;

CREATE TABLE IF NOT EXISTS todo_app.users
(
    id       bigserial,
    username varchar(30) not null unique,
    password varchar(80) not null,
    email    varchar(50) unique,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS todo_app.roles
(
    id   serial,
    name varchar(50) not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS todo_app.users_roles
(
    user_id bigint not null,
    role_id int    not null,
    primary key (user_id, role_id),
    foreign key (user_id) references todo_app.users (id),
    foreign key (role_id) references todo_app.roles (id)
);
CREATE TABLE IF NOT EXISTS todo_app.todos
(
    id bigserial,
    description varchar,
    date DATE,
    expirationDate DATE,
    todo_state varchar,
    primary key (id)
);
CREATE TABLE IF NOT EXISTS todo_app.user_todos
(
    user_id   INT NOT NULL,
    todo_id INT NOT NULL,
    primary key (user_id, todo_id),
    FOREIGN KEY (user_id) REFERENCES todo_app.users (id),
    FOREIGN KEY (todo_id) REFERENCES todo_app.todos (id)
);

insert into todo_app.roles (name)
values
    ('ROLE_USER'), ('ROLE_ADMIN');

insert into todo_app.users (username, password, email)
values
    ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'user@gmail.com'),
    ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'admin@gmail.com');

insert into todo_app.users_roles (user_id, role_id)
values
    (1, 1),
    (2, 2);

set search_path to todo_app;

insert into todo_app.todos (id, description,date,todo_state)
values
    (1, 'buy some milk', '10.08.2023', 'PLANNED');
insert into todo_app.todos (id, description,date,todo_state)
values
    (2, 'buy chocolate', '21.04.2023', 'NOTIFIED');
insert into todo_app.todos (id, description,date,todo_state)
values
    (3, 'buy car', '10.08.2023', 'WORK_IN_PROGRESS');
drop table todo_app.user_todos;
drop table todo_app.todos;

insert into todo_app.user_todos (todo_id,user_id)
values (1, 1);
insert into todo_app.user_todos (todo_id,user_id)
values (2, 2);
insert into todo_app.user_todos (todo_id,user_id)
values (3, 1);
create type TodoState as enum
(
    'Planned',
    'Work_in_progress',
    'Postponed',
    'Notified',
    'Signed',
    'Done',
    'Canceled'
    )

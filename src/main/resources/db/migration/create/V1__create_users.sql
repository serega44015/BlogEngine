-- drop table if exists users;

create table users
(
    id           integer      not null auto_increment,
    code         VARCHAR(255),
    email        VARCHAR(255) not null,
    is_moderator TINYINT      not null,
    name         VARCHAR(255) not null,
    password     VARCHAR(255) not null,
    photo        TEXT,
    reg_time     DATETIME     not null,
    primary key (id)
);


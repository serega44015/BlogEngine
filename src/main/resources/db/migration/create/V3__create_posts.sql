create table posts
(
    id                integer      not null auto_increment,
    is_active         TINYINT      not null,
    moderation_status varchar(255) not null,
    moderator_id      INT,
    text              TEXT         not null,
    time              DATETIME    not null,
    title             VARCHAR(255) not null,
    view_count        INT          not null,
    user_id           integer      not null,
    primary key (id)
);
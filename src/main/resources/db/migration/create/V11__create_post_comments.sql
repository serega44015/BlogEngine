create table posts_comments
(
    id        integer  not null auto_increment,
    parent_id INT,
    text      TEXT     not null,
    time      DATETIME not null,
    post_id   integer  not null,
    user_id   integer  not null,
    primary key (id)
)
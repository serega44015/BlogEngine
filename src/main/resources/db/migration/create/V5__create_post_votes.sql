create table post_votes
(
    id      integer  not null auto_increment,
    time    DATETIME not null,
    user_id INT      not null,
    value   TINYINT  not null,
    post_id integer  not null,
    primary key (id)
);
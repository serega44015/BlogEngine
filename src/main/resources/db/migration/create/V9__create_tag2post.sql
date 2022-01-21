create table tags2post
(
    id      integer not null auto_increment,
    post_id INT     not null,
    tag_id  INT     not null,
    primary key (id)
)
alter table post_votes
    add constraint foreign key (post_id) references posts (id);

alter table posts
    add constraint foreign key (user_id) references users (id);

alter table posts_comments
    add constraint foreign key (post_id) references posts (id);

alter table posts_comments
    add constraint foreign key (user_id) references users (id);

alter table tags2post
    add constraint foreign key (tag_id) references tags (id);

alter table tags2post
    add constraint foreign key (post_id) references posts (id);

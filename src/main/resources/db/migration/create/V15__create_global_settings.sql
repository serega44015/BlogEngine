create table global_settings
(
    id    integer      not null auto_increment,
    code  VARCHAR(255) not null,
    name  VARCHAR(255) not null,
    value VARCHAR(255) not null,
    primary key (id)
)
create table captcha_codes
(
    id          integer  not null auto_increment,
    code        TINYTEXT not null,
    secret_code TINYTEXT not null,
    time        DATETIME not null,
    primary key (id)
)
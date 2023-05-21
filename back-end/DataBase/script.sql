create table if not exists question
(
    item_id         serial
        primary key,
    question_id     integer not null
        unique,
    answer_count    integer not null,
    accepted_answer boolean not null,
    view_count      integer,
    score           integer,
    post_time       varchar,
    accept_time     varchar,
    comm_num        integer default 0,
    tags            varchar,
    tags_upvote     integer,
    tags_view       integer
);

alter table question
    owner to postgres;

create table if not exists tag
(
    item_id      serial
        constraint tag_pk
            primary key,
    tag_name     varchar
        unique,
    view_count   bigint,
    upvote_count bigint,
    appear_num   integer
);

alter table tag
    owner to postgres;

create table if not exists users
(
    item_id     serial
        constraint users_pk
            primary key,
    account_id  integer
        unique,
    user_name   varchar,
    post_num    integer,
    answer_num  integer,
    comment_num integer
);

alter table users
    owner to postgres;

create table if not exists "APIs"
(
    item_id    serial
        constraint "APIs_pk"
            primary key,
    api_name   varchar
        unique,
    appear_num integer
);

alter table "APIs"
    owner to postgres;

create table if not exists answer
(
    item_id     serial
        constraint answer_pk
            primary key,
    answer_id   integer not null
        unique,
    is_accepted boolean,
    score       integer,
    question_id integer
);

alter table answer
    owner to postgres;

create table if not exists tags_comb
(
    item_id   serial
        constraint tags_comb_pk
            primary key,
    tags_name varchar
        unique,
    upvote    integer,
    view      integer
);

alter table tags_comb
    owner to postgres;


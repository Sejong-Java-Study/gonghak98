DROP DATABASE IF EXISTS gonghak;
CREATE DATABASE gonghak default character set utf8mb4 collate utf8mb4_unicode_ci;
USE gonghak;

DROP TABLE IF EXISTS major;
DROP TABLE IF EXISTS abeek;
DROP TABLE IF EXISTS completed_course;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS gonghak_course;
DROP TABLE IF EXISTS user;

create table major
(
    major_id bigint       not null auto_increment,
    major    varchar(255) not null,
    primary key (major_id)
);

create table abeek
(
    abeek_type tinyint not null check (abeek_type between 0 and 5),
    min_credit integer not null,
    year       integer not null check ((year >= 14) and (year <= 24)),
    abeek_id   bigint  not null auto_increment,
    major_id   bigint,
    note       tinytext,
    primary key (abeek_id),
    foreign key (major_id)
        references major (major_id)
);

create table course
(
    credit    integer      not null,
    course_id bigint       not null,
    name      varchar(255) not null,
    primary key (course_id)
);

create table completed_course
(
    semester            integer not null check ((semester >= 1) and (semester <= 2)),
    year                integer not null check ((year >= 2015) and (year <= 2024)),
    completed_course_id bigint  not null auto_increment,
    course_id           bigint,
    user_id             bigint,
    primary key (completed_course_id),
    foreign key (course_id)
        references course (course_id)
);

create table gonghak_course
(
    design_credit         float(53)                                              not null check ((design_credit >= 0) and (design_credit <= 6)),
    year                  integer                                                not null check ((year >= 15) and (year <= 24)),
    course_id             bigint,
    gonghak_course_id     bigint                                                 not null auto_increment,
    major_id              bigint,
    course_category_const enum ('BSM','MSC','전공','전공기초','전공주제','전문교양','전선','전필') not null,
    pass_category         varchar(255)                                           not null,
    primary key (gonghak_course_id),
    foreign key (course_id)
        references course (course_id),
    foreign key (major_id)
        references major (major_id)
);

create table user
(
    major_id   bigint,
    student_id bigint       not null unique,
    user_id    bigint       not null auto_increment,
    email      varchar(255) not null,
    name       varchar(255) not null,
    password   varchar(255) not null,
    primary key (user_id),
    foreign key (major_id)
        references major (major_id)
);

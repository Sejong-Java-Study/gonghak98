create table MajorsDomain (
                              id bigint not null auto_increment,
                              major varchar(255) not null,
                              primary key (id)
);

create table AbeekDomain (
                             id bigint not null auto_increment,
                             abeekType tinyint not null check (abeekType between 0 and 5),
                             minCredit integer not null,
                             year integer not null check ((year>=14) and (year<=24)),
                             major_id bigint,
                             note tinytext,
                             primary key (id)
);

create table CompletedCoursesDomain (
                                        id bigint not null auto_increment,
                                        semester integer not null check ((semester>=1) and (semester<=2)),
                                        year integer not null check ((year>=15) and (year<=24)),
                                        course_id bigint,
                                        user_id bigint,
                                        primary key (id)
);

create table CoursesDomain (
                               courseId bigint not null,
                               credit integer not null,
                               name varchar(255) not null,
                               primary key (courseId)
);

create table GonghakCoursesDomain (
                                      id bigint not null auto_increment,
                                      designCredit float not null check ((designCredit>=0) and (designCredit<=6)),
                                      year integer not null check ((year>=15) and (year<=24)),
                                      course_id bigint,
                                      major_id bigint,
                                      courseCategory varchar(255) not null,
                                      passCategory varchar(255) not null,
                                      primary key (id)
);

create table UserDomain (
                            id bigint not null auto_increment,
                            major_id bigint,
                            studentId bigint not null,
                            email varchar(255) not null,
                            name varchar(255) not null,
                            password varchar(255) not null,
                            primary key (id)
);

alter table AbeekDomain
add foreign key (major_id)
references MajorsDomain (id);

alter table CompletedCoursesDomain
add foreign key (course_id)
references CoursesDomain (courseId);

alter table CompletedCoursesDomain
add foreign key (user_id)
references UserDomain (id);

alter table GonghakCoursesDomain
add foreign key (course_id)
references CoursesDomain (courseId);

alter table GonghakCoursesDomain
add foreign key (major_id)
references MajorsDomain (id);

alter table UserDomain
add foreign key (major_id)
references MajorsDomain (id);
